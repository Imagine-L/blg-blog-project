import request from '@/request'

export function getAllTags() {
  return request({
    url: '/tags',
    method: 'get',
  })
}

export function getAllTagsDetail() {
  return request({
    url: '/tags/detail',
    method: 'get',
  })
}

export function getHotTags() {
  return request({
    url: '/tags/hot',
    method: 'get',
  })
}

export function getTag(id) {
  return request({
    url: `/tags/${id}`,
    method: 'get',
  })
}

export function getTagDetail(id) {
  return request({
    url: `/tags/detail/${id}`,
    method: 'get',
  })
}

export function saveTag(data, token) {
  return request({
    headers: {'Authorization': token},
    url: '/tags/save',
    method: 'post',
    data: data
  })
}

export function deleteEmptyTag(token) {
  return request({
    headers: {'Authorization': token},
    url: '/tags/empty',
    method: 'delete',
  })
}
