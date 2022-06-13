<template>
  <el-form ref="form" :model="user" label-width="80px">
    <el-form-item label="昵称">
      <el-input v-model="user.nickname"></el-input>
    </el-form-item>
    <el-form-item label="职业">
      <el-input v-model="user.info"></el-input>
    </el-form-item>
    <el-form-item label="地址">
      <el-input v-model="user.address"></el-input>
    </el-form-item>
    <el-form-item label="邮箱">
      <el-input v-model="user.email"></el-input>
    </el-form-item>
    <el-form-item label="简介">
      <el-input type="textarea" v-model="user.description"></el-input>
    </el-form-item>
    <el-form-item label="头像">
      <el-upload
        :action="uploadUrl"
        list-type="picture-card"
        :headers="{'Authorization': token}"
        ref="upload"
        :limit="1"
        :data="user"
        name="avatar"
        :accept="acceptSuffix"
        :auto-upload="false">
        <i class="el-icon-plus"></i>
      </el-upload>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="doUpdate">更新</el-button>
      <el-button @click="reset">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {upload} from "../../api/upload";
import {mapState} from 'vuex'
import {updateUser} from "../../api/user";

export default {
  data() {
    return {
      user: {
        nickname: this.$store.state.name,
        address: this.$store.state.address,
        email: this.$store.state.email,
        description: this.$store.state.description,
        info: this.$store.state.info
      },
      uploadUrl: 'users/update',
      acceptSuffix: '.jpg, .png, .gif, .jpeg, .svg, .ico',
    }
  },
  computed: {
    token() {
      return this.$store.state.token
    }
  },
  methods: {
    doUpdate() {
      console.log(this.$refs.upload.uploadFiles[0] !== undefined)
      if (this.$refs.upload.uploadFiles[0] !== undefined) {
        this.$refs.upload.submit();
        this.$message({type: 'success', message: '信息修改成功', showClose: true})
        this.$router.go(0)
      } else {
        updateUser(this.user, this.token).then(resp => {
          if (resp.success) {
            this.$message({type: 'success', message: '信息修改成功', showClose: true})
            this.$store.dispatch('getUserInfo')
          }
        })
      }
    },
    reset() {
      this.user = {
        nickname: this.$store.state.name,
        address: this.$store.state.address,
        email: this.$store.state.email,
        description: this.$store.state.description,
        info: this.$store.state.info
      }
    }
  }
}
</script>

<style>
</style>
