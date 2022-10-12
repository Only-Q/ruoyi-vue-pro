import request from '@/utils/request'

// 创建生物样品入库登记
export function createBiologySample(data) {
  return request({
    url: '/system/biology-sample/create',
    method: 'post',
    data: data
  })
}

// 更新生物样品入库登记
export function updateBiologySample(data) {
  return request({
    url: '/system/biology-sample/update',
    method: 'put',
    data: data
  })
}

// 删除生物样品入库登记
export function deleteBiologySample(id) {
  return request({
    url: '/system/biology-sample/delete?id=' + id,
    method: 'delete'
  })
}

// 获得生物样品入库登记
export function getBiologySample(id) {
  return request({
    url: '/system/biology-sample/get?id=' + id,
    method: 'get'
  })
}

// 获得生物样品入库登记分页
export function getBiologySamplePage(query) {
  return request({
    url: '/system/biology-sample/page',
    method: 'get',
    params: query
  })
}

// 导出生物样品入库登记 Excel
export function exportBiologySampleExcel(query) {
  return request({
    url: '/system/biology-sample/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
