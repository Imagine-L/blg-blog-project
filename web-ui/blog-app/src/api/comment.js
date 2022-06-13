import request from '@/request'


export function getCommentsByArticle(id) {
  return request({
    url: `/comments/article/${id}`,
    method: 'get'
  })
}

export function publishComment(comment, token) {
  return request({
    headers: {'Authorization': token},
    url: '/comments/create/change',
    method: 'post',
    data: comment
  })
}

export function deleteComment(data, token) {
  return request({
    headers: {'Authorization': token},
    url: `/comments/delete/`,
    method: 'delete',
    data: data
  })
}

export function getCommentByAuthor(page, pageSize, token) {
  return request({
    headers: {'Authorization': token},
    url: '/comments/author',
    method: 'post',
    data: {
      page,
      pageSize
    }
  })
}
