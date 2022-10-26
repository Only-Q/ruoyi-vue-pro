<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="体检号" prop="checkupNo">
        <el-input v-model="queryParams.checkupNo" placeholder="请输入体检号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>-->
      <el-form-item label="X线号" prop="xNo">
        <el-input v-model="queryParams.xNo" placeholder="请输入X线号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item label="血常规样本号" prop="bloodNo">
        <el-input v-model="queryParams.bloodNo" placeholder="请输入血常规样本号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="生化样本号" prop="biochemistryNo">
        <el-input v-model="queryParams.biochemistryNo" placeholder="请输入生化样本号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="尿常规样本号" prop="urineNo">
        <el-input v-model="queryParams.urineNo" placeholder="请输入尿常规样本号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="血清样本位置" prop="serumLocation">
        <el-input v-model="queryParams.serumLocation" placeholder="请输入血清样本位置" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="血浆样本位置" prop="plasmaLocation">
        <el-input v-model="queryParams.plasmaLocation" placeholder="请输入血浆样本位置" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="血细胞样本位置" prop="bloodCellsLocation">
        <el-input v-model="queryParams.bloodCellsLocation" placeholder="请输入血细胞样本位置" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="尿样样本位置" prop="urineLocation">
        <el-input v-model="queryParams.urineLocation" placeholder="请输入尿样样本位置" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>-->
      <el-form-item label="体检日期" prop="checkupTime">
        <el-date-picker v-model="queryParams.checkupTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
<!--      <el-form-item label="登记日期" prop="registerTime">
        <el-date-picker v-model="queryParams.registerTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="queryParams.remark" placeholder="请输入备注" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['system:biology-sample:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport1">导入基础信息</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport2">导入样本号</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:biology-sample:export']">导出</el-button>
      </el-col>
      <el-col :span="5">
        <el-input v-model="queryParams.sampleNo" size="small" placeholder="请扫描样本号后 点击回车 登记样本位置" clearable @keyup.enter.native="recordLocation(queryParams.sampleNo)"/>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="id" align="center" prop="id" v-if="false"/>
      <el-table-column label="体检日期" align="center" prop="checkupTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkupTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="体检号" align="center" prop="checkupNo" width="120"/>
      <el-table-column label="姓名" align="center" prop="name"/>
      <el-table-column label="性别" align="center" prop="sex" width="80">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="scope.row.sex" />
        </template>
      </el-table-column>
      <el-table-column label="X线号" align="center" prop="xNo" width="150"/>
      <el-table-column label="血常规样本号" align="center" prop="bloodNo" width="150" v-show="false"/>
      <el-table-column label="生化样本号" align="center" prop="biochemistryNo" width="150" />
      <el-table-column label="尿常规样本号" align="center" prop="urineNo" width="150" />
      <el-table-column label="血清样本位置" align="center" prop="serumLocation" width="110" />
      <el-table-column label="血浆样本位置" align="center" prop="plasmaLocation" width="110" />
      <el-table-column label="血细胞样本位置" align="center" prop="bloodCellsLocation" width="130" />
      <el-table-column label="尿样样本位置" align="center" prop="urineLocation" width="110" />
      <el-table-column label="登记日期" align="center" prop="registerTime" width="180" v-if="false">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="150"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" v-if="false">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:biology-sample:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:biology-sample:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" :inline="true">
        <el-form-item label="体检号" prop="checkupNo">
          <el-input v-model="form.checkupNo" placeholder="请输入体检号" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                      :key="dict.value" :label="dict.value" :disabled="modifyFlag">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="X线号" prop="xNo">
          <el-input v-model="form.xNo" placeholder="请输入X线号" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="血常规样本号" prop="bloodNo">
          <el-input v-model="form.bloodNo" placeholder="请输入血常规样本号" :disabled="modifyFlag" />
        </el-form-item>
        <el-form-item label="生化样本号" prop="biochemistryNo">
          <el-input v-model="form.biochemistryNo" placeholder="请输入生化样本号" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="尿常规样本号" prop="urineNo">
          <el-input v-model="form.urineNo" placeholder="请输入尿常规样本号" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="血清样本位置" prop="serumLocation" class="serum">
          <el-input v-model="form.serumLocation" maxlength="8" show-word-limit placeholder="请输入血清样本位置" :disabled="sampleType == 'bloodNo' || sampleType == 'urineNo'"/>
        </el-form-item>
        <el-form-item label="尿样样本位置" prop="urineLocation" class="urine">
          <el-input v-model="form.urineLocation" maxlength="8" show-word-limit placeholder="请输入尿样样本位置" :disabled="sampleType == 'bloodNo' || sampleType == 'biochemistryNo'"/>
        </el-form-item>
        <el-form-item label="血浆样本位置" prop="plasmaLocation" class="plasma">
          <el-input v-model="form.plasmaLocation" maxlength="8" show-word-limit placeholder="请输入血浆样本位置" :disabled="sampleType == 'biochemistryNo' || sampleType == 'urineNo'"/>
        </el-form-item>
        <el-form-item label="血细胞样本位置" prop="bloodCellsLocation" class="bloodcells">
          <el-input v-model="form.bloodCellsLocation" maxlength="8" show-word-limit placeholder="请输入血细胞样本位置" :disabled="sampleType == 'biochemistryNo' || sampleType == 'urineNo'"/>
        </el-form-item>
        <el-form-item label="体检日期" prop="checkupTime">
          <el-date-picker clearable v-model="form.checkupTime" type="date" value-format="timestamp" placeholder="选择体检日期" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="登记日期" prop="registerTime">
          <el-date-picker clearable v-model="form.registerTime" type="date" value-format="timestamp" placeholder="选择登记日期" :disabled="modifyFlag"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="4" v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="打印预览" prop="remark">
          <div id="printView" class="print-view">
            <div style="margin-left:25px">{{ form.name }}</div>
            <div style="margin-left:25px">{{formartDate(form.checkupTime)}}</div>
            <div style="margin-left:25px">{{form.xNo}}</div>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" plain v-print="'#printView'" icon="el-icon-printer" style="margin-left:35px;margin-top:20px">打印</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                 :action="upload.url + '?type=' + upload.type" :disabled="upload.isUploading"
                 :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <el-input v-model="upload.type" v-show="false"/>
          <span>仅允许导入xls、xlsx格式文件。</span>
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
import { createBiologySample, updateBiologySample, deleteBiologySample, getBiologySample, getBiologySamplePage, exportBiologySampleExcel,getSampleInfo } from "@/api/system/biologySample";
import { getBaseHeader } from '@/utils/request'

export default {
  name: "BiologySample",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 生物样品入库登记列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        checkupNo: null,
        name: null,
        sex: null,
        xNo: null,
        sampleNo: null,
        bloodNo: null,
        biochemistryNo: null,
        urineNo: null,
        serumLocation: null,
        plasmaLocation: null,
        bloodCellsLocation: null,
        urineLocation: null,
        checkupTime: [],
        registerTime: [],
        remark: null,
        createTime: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        checkupNo: [{ required: true, message: "体检号不能为空", trigger: "blur" }],
        name: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        sex: [{ required: true, message: "性别不能为空", trigger: "blur" }],
        xNo: [{ required: true, message: "X线号不能为空", trigger: "blur" }],
        checkupTime: [{ required: true, message: "体检日期不能为空", trigger: "blur" }],
        serumLocation: [{ pattern: /^\w{1}$|^\w{8}$/, message: "长度需为8位", trigger: "blur" }],
        urineLocation: [{ pattern: /^\w{1}$|^\w{8}$/, message: "长度需为8位", trigger: "blur" }],
        plasmaLocation: [{ pattern: /^\w{1}$|^\w{8}$/, message: "长度需为8位", trigger: "blur" }],
        bloodCellsLocation: [{ pattern: /^\w{1}$|^\w{8}$/, message: "长度需为8位", trigger: "blur" }],
      },
      // 导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 导入类型：1-基础信息导入；2-样本号导入
        type: "",
        // 设置上传的请求头部
        headers: getBaseHeader(),
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/admin-api/system/biology-sample/import'
      },
      sampleType: "",
      modifyFlag: true
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 导入按钮操作 */
    handleImport1() {
      this.upload.title = "基础信息导入";
      this.upload.open = true;
      this.upload.type = "1";
    },
    handleImport2() {
      this.upload.title = "样本号导入";
      this.upload.open = true;
      this.upload.type = "2";
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getBiologySamplePage(this.queryParams).then(response => {
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
        checkupNo: undefined,
        name: undefined,
        sex: undefined,
        xNo: undefined,
        bloodNo: undefined,
        biochemistryNo: undefined,
        urineNo: undefined,
        serumLocation: undefined,
        plasmaLocation: undefined,
        bloodCellsLocation: undefined,
        urineLocation: undefined,
        checkupTime: undefined,
        registerTime: undefined,
        remark: undefined,
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
      this.modifyFlag = false;
      this.sampleType = "";
      this.reset();
      this.form.serumLocation = "Q";
      this.form.plasmaLocation = "J";
      this.form.bloodCellsLocation = "X";
      this.form.urineLocation = "N";
      this.open = true;
      this.title = "添加生物样品入库登记";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.modifyFlag = false;
      this.sampleType = "";
      this.reset();
      const id = row.id;
      getBiologySample(id).then(response => {
        this.form = response.data;
        if(this.form.serumLocation == null || this.form.serumLocation == ""){
          this.form.serumLocation = "Q"
        }
        if(this.form.plasmaLocation == null || this.form.plasmaLocation == ""){
          this.form.plasmaLocation = "J"
        }
        if(this.form.bloodCellsLocation == null || this.form.bloodCellsLocation == ""){
          this.form.bloodCellsLocation = "X"
        }
        if(this.form.urineLocation == null || this.form.urineLocation == ""){
          this.form.urineLocation = "N"
        }
        this.open = true;
        this.title = "修改生物样品入库登记";
      });
    },
    /** 扫描后回车 **/
    recordLocation(sampleNo){
      this.modifyFlag = true;
      getSampleInfo(sampleNo).then(response => {
        let map = response.data;
        if(map.code == "9999"){
          this.$modal.msgError("根据样本号未查询到数据或查询到多条数据");
          this.queryParams.sampleNo = "";
        }else{
          this.sampleType = map.sampleType;
          this.reset();
          const id = map.id;
          getBiologySample(id).then(response => {
            this.form = response.data;
            if(this.form.serumLocation == null || this.form.serumLocation == ""){
              this.form.serumLocation = "Q"
            }
            if(this.form.plasmaLocation == null || this.form.plasmaLocation == ""){
              this.form.plasmaLocation = "J"
            }
            if(this.form.bloodCellsLocation == null || this.form.bloodCellsLocation == ""){
              this.form.bloodCellsLocation = "X"
            }
            if(this.form.urineLocation == null || this.form.urineLocation == ""){
              this.form.urineLocation = "N"
            }
            this.open = true;
            this.title = "修改生物样品入库登记";
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        if(this.form.serumLocation == "Q"){
          this.form.serumLocation = "";
        }
        if(this.form.plasmaLocation == "J"){
          this.form.plasmaLocation = ""
        }
        if(this.form.bloodCellsLocation == "X"){
          this.form.bloodCellsLocation = ""
        }
        if(this.form.urineLocation == "N"){
          this.form.urineLocation = ""
        }
        // 修改的提交
        if (this.form.id != null) {
          updateBiologySample(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createBiologySample(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除生物样品入库登记编号为"' + id + '"的数据项?').then(function() {
          return deleteBiologySample(id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有生物样品入库登记数据项?').then(() => {
          this.exportLoading = true;
          return exportBiologySampleExcel(params);
        }).then(response => {
          this.$download.excel(response, '生物样品入库登记.xls');
          this.exportLoading = false;
        }).catch(() => {});
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
    formartDate(param) {
     let date = new Date(param);
     let Y = date.getFullYear() + '-';
     let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
     let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
     let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
     let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
     let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
     return Y + M + D;
    }
  }
};
</script>
<style>
  @page{
    size: auto;
    margin: 0mm;
  }
  .serum .el-form-item__label{
    color: green;
  }
  .plasma .el-form-item__label{
    color: yellow;
  }
  .bloodcells .el-form-item__label{
    color: grey;
  }
  .urine .el-form-item__label{
    color: blue;
  }
  .print-view{
    line-height: 20px;
  }
</style>
