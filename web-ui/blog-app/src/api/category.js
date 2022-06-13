import request from '@/request'

export function getAllCategorys() {
  return request({
    url: '/categorys',
    method: 'get',
  })
}

export function getAllCategorysDetail() {
  return request({
    url: '/categorys/detail',
    method: 'get',
  })
}

export function getCategory(id) {
  return request({
    url: `/categorys/${id}`,
    method: 'get',
  })
}

export function getCategoryDetail(id) {
  return request({
    url: `/categorys/detail/${id}`,
    method: 'get',
  })
}

export function saveCategory(data, token) {
  return request({
    headers: {'Authorization': token},
    url: '/categorys/save',
    method: 'post',
    data: data
  })
}

export function deleteEmptyCategory(token) {
  return request({
    headers: {'Authorization': token},
    url: '/categorys/empty',
    method: 'delete',
  })
}
