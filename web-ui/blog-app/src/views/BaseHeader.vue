<template>
  <el-header class="me-area">
    <el-row class="me-header">

      <el-col :span="4" class="me-header-left">
        <router-link to="/" class="me-title">
          <img src="static/img/logo.4310870.png" id="logo"/>
        </router-link>
      </el-col>

      <el-col v-if="!simple" :span="11">
        <el-menu :router=true menu-trigger="click" active-text-color="#5FB878" :default-active="activeIndex"
                 mode="horizontal">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/category/all">文章分类</el-menu-item>
          <el-menu-item index="/tag/all">标签</el-menu-item>
          <el-menu-item index="/archives">文章归档</el-menu-item>
          <el-menu-item index="/personal">个人中心</el-menu-item>

          <el-col :span="4" >
            <el-menu-item index="/write"><i class="el-icon-edit"></i>写文章</el-menu-item>
          </el-col>

        </el-menu>
      </el-col>

      <template v-else>
        <slot></slot>
      </template>

      <!-- 搜索框 -->
      <el-col :span="5">
        <el-menu mode="horizontal" active-text-color="#5FB878">
          <el-menu-item>
            <template>
              <el-autocomplete
                v-model="search"
                :fetch-suggestions="querySearchAsync"
                placeholder="请输入文章标题"
                @select="handleSelect"
                clearable
              ></el-autocomplete>
            </template>
          </el-menu-item>
        </el-menu>
      </el-col>

      <el-col :span="4">
        <el-menu :router=true menu-trigger="click" mode="horizontal" active-text-color="#5FB878">

          <template v-if="!user.login">
            <el-menu-item index="/login">
              <el-button type="text">登录</el-button>
            </el-menu-item>
            <el-menu-item index="/register">
              <el-button type="text">注册</el-button>
            </el-menu-item>
          </template>

          <template v-else>
            <el-submenu index>
              <template slot="title">
                <img class="me-header-picture" :src="user.avatar"/>
              </template>
              <el-menu-item index @click="logout"><i class="el-icon-back"></i>退出</el-menu-item>
            </el-submenu>
          </template>
        </el-menu>
      </el-col>

    </el-row>
  </el-header>
</template>

<script>
import {searchArticle} from "../api/article";

export default {
  name: 'BaseHeader',
  props: {
    activeIndex: String,
    simple: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      search: ''
    }
  },
  computed: {
    user() {
      let login = this.$store.state.account.length != 0
      let avatar = this.$store.state.avatar
      return {
        login, avatar
      }
    }
  },
  methods: {
    logout() {
      let that = this
      this.$store.dispatch('logout').then(() => {
        this.$router.push({path: '/'})
      }).catch((error) => {
        if (error !== 'error') {
          that.$message({message: error, type: 'error', showClose: true});
        }
      })
    },
    querySearchAsync(queryString, cb) {
      let that = this
      searchArticle(queryString).then(resp => {
        if (resp.success) {
          let results = []
          for (const item of resp.data) {
            console.log(item)
            results.push({
              id: item.id,
              value: item.title
            })
          }
          cb(results)
        }
      }).cache(err => {
        that.$message({message: '搜索框请求失败', type: 'error', showClose: true});
      })
    },
    handleSelect(item) {
      this.$router.push({path: `/view/${item.id}`})
      this.search = ''
    }
  }
}
</script>

<style>

.el-header {
  position: fixed;
  z-index: 1024;
  min-width: 100%;
  box-shadow: 0 2px 3px hsla(0, 0%, 7%, .1), 0 0 0 1px hsla(0, 0%, 7%, .1);
}

.me-title {
  margin-top: 10px;
  font-size: 24px;
}

.me-header-left {
  margin-top: 10px;
}

.me-title img {
  max-height: 2.4rem;
  max-width: 100%;
}

.me-header-picture {
  width: 36px;
  height: 36px;
  border: 1px solid #ddd;
  border-radius: 50%;
  vertical-align: middle;
  background-color: #5fb878;
}

#logo {
  width: 100px;
  /*height: 120px;*/
  padding-left: 50px;
}
</style>
