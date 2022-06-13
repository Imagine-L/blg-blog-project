import request from '@/request'

export function upload(formdata, token) {
  return request({
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': token
    },
    url: '/upload',
    method: 'post',
    data: formdata
  })
}
