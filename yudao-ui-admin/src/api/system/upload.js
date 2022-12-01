import request from '@/utils/request'

// 创建文件上传
export function createUpload(data) {
  return request({
    url: '/system/upload/create',
    method: 'post',
    data: data
  })
}

// 更新文件上传
export function updateUpload(data) {
  return request({
    url: '/system/upload/update',
    method: 'put',
    data: data
  })
}

// 删除文件上传
export function deleteUpload(id) {
  return request({
    url: '/system/upload/delete?id=' + id,
    method: 'delete'
  })
}

// 获得文件上传
export function getUpload(id) {
  return request({
    url: '/system/upload/get?id=' + id,
    method: 'get'
  })
}

// 获得文件上传分页
export function getUploadPage(query) {
  return request({
    url: '/system/upload/page',
    method: 'get',
    params: query
  })
}

// 导出文件上传 Excel
export function exportUploadExcel(query) {
  return request({
    url: '/system/upload/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 下载解析结果
export function downloadFile(id) {
  return request({
    url: '/system/upload/downFile?id=' + id,
    method: 'get',
    responseType: 'blob'
  })
}
