import Vuex from 'vuex'
import Vue from 'vue'
import {getToken, setToken, removeToken} from '@/request/token'
import {login, getUserInfo, logout, register} from '@/api/login'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    id: '',
    account: '',
    name: '',
    avatar: 'http://blog.liubaiblog.top/blog/default_avatar.png',
    address: '未知',
    email: '',
    description: '未知',
    info: '未知',
    token: getToken(),
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_ACCOUNT: (state, account) => {
      state.account = account
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_EMAIL: (state, email) => {
      state.email = email
    },
    SET_ADDRESS: (state, address) => {
      state.address = address
    },
    SET_DESCRIPTION: (state, description) => {
      state.description = description
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
  },
  actions: {
    login({commit}, user) {
      return new Promise((resolve, reject) => {
        login(user.account, user.password).then(data => {
          if (data.success) {
            commit('SET_TOKEN', data.data)
            setToken(data.data)
            resolve()
          } else {
            reject(data.msg)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    getUserInfo({commit, state}) {
      let that = this
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(data => {
          if (data.success) {
            commit('SET_ACCOUNT', data.data.account)
            commit('SET_NAME', data.data.nickname)
            commit('SET_AVATAR', data.data.avatar)
            commit('SET_ID', data.data.id)
            commit('SET_EMAIL', data.data.email)
            if (data.data.address === null || data.data.address === '') {
              commit('SET_ADDRESS', '未知')
            } else {
              commit('SET_ADDRESS', data.data.address)
            }
            if (data.data.description === null || data.data.description === '') {
              commit('SET_DESCRIPTION', '未知')
            } else {
              commit('SET_DESCRIPTION', data.data.description)
            }
            if (data.data.info === null || data.data.info === '') {
              commit('SET_INFO', '未知')
            } else {
              commit('SET_INFO', data.data.info)
            }
            resolve(data)
          } else {
            commit('SET_ACCOUNT', '')
            commit('SET_NAME', '')
            commit('SET_AVATAR', 'http://blog.liubaiblog.top/blog/default_avatar.png')
            commit('SET_ID', '')
            commit('SET_EMAIL', '')
            commit('SET_ADDRESS', '未知')
            commit('SET_DESCRIPTION', '未知')
            commit('SET_INFO', '未知')
            removeToken()
            resolve(data)
          }
        }).catch(error => {
          commit('SET_ACCOUNT', '')
          commit('SET_NAME', '')
          commit('SET_AVATAR', 'http://blog.liubaiblog.top/blog/default_avatar.png')
          commit('SET_ID', '')
          commit('SET_EMAIL', '')
          commit('SET_ADDRESS', '未知')
          commit('SET_DESCRIPTION', '未知')
          commit('SET_INFO', '未知')
          removeToken()
          reject(error)
        })
      })
    },
    // 退出
    logout({commit, state}) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(data => {
          if (data.success) {
            commit('SET_TOKEN', '')
            commit('SET_ACCOUNT', '')
            commit('SET_NAME', '')
            commit('SET_EMAIL', '')
            commit('SET_AVATAR', 'http://blog.liubaiblog.top/blog/default_avatar.png')
            commit('SET_ID', '')
            commit('SET_ADDRESS', '未知')
            commit('SET_DESCRIPTION', '未知')
            commit('SET_INFO', '未知')
            removeToken()
            resolve()
          }

        }).catch(error => {
          reject(error)
        })
      })
    },
    // 前端 登出
    fedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_ACCOUNT', '')
        commit('SET_NAME', '')
        commit('SET_AVATAR', 'http://blog.liubaiblog.top/blog/default_avatar.png')
        commit('SET_ID', '')
        commit('SET_EMAIL', '')
        commit('SET_ADDRESS', '未知')
        commit('SET_DESCRIPTION', '未知')
        commit('SET_INFO', '未知')
        removeToken()
        resolve()
      }).catch(error => {
        reject(error)
      })
    },
    register({commit}, user) {
      return new Promise((resolve, reject) => {
        register(user.account, user.nickname, user.password).then((data) => {
          if (data.success) {
            commit('SET_TOKEN', data.data)
            setToken(data.data)
            resolve()
          } else {
            reject(data.msg)
          }
        }).catch((error) => {
          reject(error)
        })
      })
    }
  }
})
