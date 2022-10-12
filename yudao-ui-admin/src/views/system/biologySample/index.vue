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
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="X线号" prop="xNo">
        <el-input v-model="queryParams.xNo" placeholder="请输入X线号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="血常规样本号" prop="bloodNo">
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
      </el-form-item>
      <el-form-item label="体检日期" prop="checkupTime">
        <el-date-picker v-model="queryParams.checkupTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="登记日期" prop="registerTime">
        <el-date-picker v-model="queryParams.registerTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="queryParams.remark" placeholder="请输入备注" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
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
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:biology-sample:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="体检号" align="center" prop="checkupNo" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="sex">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="scope.row.sex" />
        </template>
      </el-table-column>
      <el-table-column label="X线号" align="center" prop="xNo" />
      <el-table-column label="血常规样本号" align="center" prop="bloodNo" />
      <el-table-column label="生化样本号" align="center" prop="biochemistryNo" />
      <el-table-column label="尿常规样本号" align="center" prop="urineNo" />
      <el-table-column label="血清样本位置" align="center" prop="serumLocation" />
      <el-table-column label="血浆样本位置" align="center" prop="plasmaLocation" />
      <el-table-column label="血细胞样本位置" align="center" prop="bloodCellsLocation" />
      <el-table-column label="尿样样本位置" align="center" prop="urineLocation" />
      <el-table-column label="体检日期" align="center" prop="checkupTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkupTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登记日期" align="center" prop="registerTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="体检号" prop="checkupNo">
          <el-input v-model="form.checkupNo" placeholder="请输入体检号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                      :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="X线号" prop="xNo">
          <el-input v-model="form.xNo" placeholder="请输入X线号" />
        </el-form-item>
        <el-form-item label="血常规样本号" prop="bloodNo">
          <el-input v-model="form.bloodNo" placeholder="请输入血常规样本号" />
        </el-form-item>
        <el-form-item label="生化样本号" prop="biochemistryNo">
          <el-input v-model="form.biochemistryNo" placeholder="请输入生化样本号" />
        </el-form-item>
        <el-form-item label="尿常规样本号" prop="urineNo">
          <el-input v-model="form.urineNo" placeholder="请输入尿常规样本号" />
        </el-form-item>
        <el-form-item label="血清样本位置" prop="serumLocation">
          <el-input v-model="form.serumLocation" placeholder="请输入血清样本位置" />
        </el-form-item>
        <el-form-item label="血浆样本位置" prop="plasmaLocation">
          <el-input v-model="form.plasmaLocation" placeholder="请输入血浆样本位置" />
        </el-form-item>
        <el-form-item label="血细胞样本位置" prop="bloodCellsLocation">
          <el-input v-model="form.bloodCellsLocation" placeholder="请输入血细胞样本位置" />
        </el-form-item>
        <el-form-item label="尿样样本位置" prop="urineLocation">
          <el-input v-model="form.urineLocation" placeholder="请输入尿样样本位置" />
        </el-form-item>
        <el-form-item label="体检日期" prop="checkupTime">
          <el-date-picker clearable v-model="form.checkupTime" type="date" value-format="timestamp" placeholder="选择体检日期" />
        </el-form-item>
        <el-form-item label="登记日期" prop="registerTime">
          <el-date-picker clearable v-model="form.registerTime" type="date" value-format="timestamp" placeholder="选择登记日期" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createBiologySample, updateBiologySample, deleteBiologySample, getBiologySample, getBiologySamplePage, exportBiologySampleExcel } from "@/api/system/biologySample";

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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
      this.reset();
      this.open = true;
      this.title = "添加生物样品入库登记";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getBiologySample(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改生物样品入库登记";
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
    }
  }
};
</script>
