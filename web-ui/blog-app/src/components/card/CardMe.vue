<template>
  <el-card>
    <h1 class="me-author-name">博乐阁</h1>
    <div v-if="this.$store.state.token !== undefined">
      <div class="me-author-description">
        <div>
          <img :src="avatar" id="avatar" alt="头像"/>
        </div>
        <div>
          <p class="username">{{ name }}</p>
        </div>
        <div class="msg">
          <p>{{ info }}</p>
          <p>{{ address }}</p>
        </div>
        <div>
          <p class="description">{{ description }}</p>
        </div>
        <!--        <div class="description">-->
        <!--          <p>{{ description }}</p>-->
        <!--        </div>-->
        <!--        <div>-->
        <!--          <span><i class="el-icon-location"></i> &nbsp;{{ name }}</span>-->
        <!--          <span><i class="el-icon-location"></i> &nbsp;{{ address }}</span>-->
        <!--          <span><i class="el-icon-star-on"></i> &nbsp;{{ info }}</span>-->
        <!--        </div>-->
      </div>
      <!--      <br><br>-->

    </div>
    <div class="me-author-description" v-if="this.$store.state.token === undefined">
      <div>
        <img src="static/img/logo.4310870.png" width="200px">
      </div>
      <div>
        <p class="username" @click="toLogin" style="cursor: pointer; color: #3cb644">游客请登录</p>
      </div>
      <div>
        <p class="description">遇见博乐阁，遇见你的伯乐</p>
      </div>
    </div>
  </el-card>

</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'CardMe',
  data() {
    return {
      circleUrl: 'http://blog.liubaiblog.top/blog/default_avatar.png'
    }
  },
  computed: mapState(['name', 'address', 'description', 'info', 'avatar']),
  methods: {
    toLogin() {
      this.$router.push({path: '/login'})
    }
  }
}
</script>

<style scoped>
.me-author-name {
  text-align: center;
  font-size: 30px;
  border-bottom: 1px solid #5FB878;
}

.me-author-description {
  padding: 10px 0;
  text-align: center;
}

/*.me-author-description > * {*/
/*  float: left;*/
/*}*/

/*.me-author-description > div {*/
/*  margin-top: 8px;*/
/*  margin-left: 20px;*/
/*}*/

.me-author-tool i {
  cursor: pointer;
  /*padding: 4px 10px;*/
  font-size: 30px;
}

#avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  margin-top: 2px;
}

.username {
  font-size: 17px;
  font-weight: bold;
  font-family: "微软雅黑", serif;
}

.description {
  margin-top: 15px;
  font-size: 13px;
  font-family: "仿宋", serif;
  color: #afafac;
  text-align: center;
}

.msg {
  margin: 5px 0;
  font-size: 14px;
  font-weight: bold;
  color: #656363;
}
</style>
