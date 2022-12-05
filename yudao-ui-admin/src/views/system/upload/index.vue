<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名" prop="fileName">
        <el-input v-model="queryParams.fileName" placeholder="请输入文件名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="解析状态" prop="analysisStatus">
        <el-select v-model="queryParams.analysisStatus" placeholder="请选择解析状态" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.ANALYSIS_STATUS)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="解析失败原因" prop="failReason">
        <el-input v-model="queryParams.failReason" placeholder="请输入解析失败原因" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="Excel路径" prop="excelPath">
        <el-input v-model="queryParams.excelPath" placeholder="请输入Excel路径" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item> -->
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <!-- <el-form-item label="解析进度" prop="analysisSpeed">
        <el-input v-model="queryParams.analysisSpeed" placeholder="请输入解析进度" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['system:upload:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:upload:export']">导出</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="文件名" align="center" prop="fileName" />
      <el-table-column label="解析状态" align="center" prop="analysisStatus">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.ANALYSIS_STATUS" :value="scope.row.analysisStatus" />
        </template>
      </el-table-column>
      <el-table-column label="解析失败原因" align="center" prop="failReason" show-overflow-tooltip/>
<!--      <el-table-column label="Excel路径" align="center" prop="excelPath" />-->
      <el-table-column label="解析进度" align="center" prop="analysisSpeed" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
<!--          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:upload:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:upload:delete']">删除</el-button>-->
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownload(scope.row)"
          :loading="downloadLoading">下载</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名" />
        </el-form-item>
        <el-form-item label="解析状态" prop="analysisStatus">
          <el-select v-model="form.analysisStatus" placeholder="请选择解析状态">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.ANALYSIS_STATUS)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="解析失败原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入解析失败原因" />
        </el-form-item>
        <el-form-item label="Excel路径" prop="excelPath">
          <el-input v-model="form.excelPath" placeholder="请输入Excel路径" />
        </el-form-item>
        <el-form-item label="解析进度" prop="analysisSpeed">
          <el-input v-model="form.analysisSpeed" placeholder="请输入解析进度" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".zip" :headers="upload.headers" :file-list="fileList"
                 :action="upload.url" :disabled="upload.isUploading" :before-upload="beforeAvatarUpload"
                 :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <el-input v-model="upload.type" v-show="false"/>
          <span>仅允许导入zip文件。</span>
          <!--          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>-->
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createUpload, updateUpload, deleteUpload, getUpload, getUploadPage, exportUploadExcel,downloadFile } from "@/api/system/upload";
import { getBaseHeader } from '@/utils/request'
export default {
  name: "Upload",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 下载解析结果遮罩层
      downloadLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 文件上传列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        fileName: null,
        analysisStatus: null,
        failReason: null,
        excelPath: null,
        createTime: [],
        analysisSpeed: null,
      },
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: getBaseHeader(),
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/admin-api/system/upload/import'
      },
      fileList: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fileName: [{ required: true, message: "文件名不能为空", trigger: "blur" }],
        analysisStatus: [{ required: true, message: "解析状态不能为空", trigger: "change" }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "体检报告导入";
      this.upload.open = true;
      this.fileList = [];
    },
    //文件上传前处理
    beforeAvatarUpload(file) {
      const isZIP = file.name.substring(file.name.lastIndexOf('.')+1) === 'zip';
      const isLt200M = file.size / 1024 / 1024 < 200;

      if (!isZIP) {
        this.$message.error('上传文件只能是 ZIP 格式!');
      }
      if (!isLt200M) {
        this.$message.error('上传文件大小不能超过 200MB!');
      }
      return isZIP && isLt200M;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      if (response.code !== 0) {
        this.$modal.msgError(response.msg)
        return;
      }
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      // 拼接提示语
      let data = response.data;
      this.$modal.msgSuccess(data);
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getUploadPage(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        fileName: undefined,
        analysisStatus: undefined,
        failReason: undefined,
        excelPath: undefined,
        analysisSpeed: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文件上传";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getUpload(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文件上传";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.id != null) {
          updateUpload(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createUpload(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除文件上传编号为"' + id + '"的数据项?').then(function() {
          return deleteUpload(id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 下载解析结果 */
    handleDownload(row) {
      const fileName = row.fileName;
      this.$modal.confirm('是否确认下载'+fileName+'的解析结果?').then(() => {
        this.downloadLoading = true;
        downloadFile(row.id).then(res => {
          this.$download.excel(res, fileName.split(".")[0]+'.xlsx');
          this.downloadLoading = false;
        });
      }).catch(() => {this.downloadLoading = false;});

    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有文件上传数据项?').then(() => {
          this.exportLoading = true;
          return exportUploadExcel(params);
        }).then(response => {
          this.$download.excel(response, '文件上传.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
