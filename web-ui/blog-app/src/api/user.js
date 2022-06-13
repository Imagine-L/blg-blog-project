import request from "@/request";

export function changePwd(oldPwd, newPwd, token) {
  return request({
    headers: {'Authorization': token},
    url: '/users/pwd',
    method: 'post',
    data: {
      oldPwd,
      newPwd
    }
  })
}

export function updateUser(data, token) {
  return request({
    headers: {'Authorization': token},
    url: '/users/update',
    method: 'put',
    data: data
  })
}
