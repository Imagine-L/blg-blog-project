<template>
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="原密码" prop="oldPwd">
      <el-input type="password" v-model="ruleForm.oldPwd" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="newPwd">
      <el-input type="password" v-model="ruleForm.newPwd" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="确认新密码" prop="checkPass">
      <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {changePwd} from "../../api/user";

export default {
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        let field = '原密码'
        if (rule.field === 'newPwd') field = '新密码'
        callback(new Error(`请输入${field}`));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'));
      } else if (value !== this.ruleForm.newPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        oldPwd: '',
        newPwd: '',
        checkPass: '',
      },
      rules: {
        oldPwd: [
          {validator: validatePass, trigger: 'blur'}
        ],
        newPwd: [
          {validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'}
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          changePwd(this.ruleForm.oldPwd, this.ruleForm.newPwd,this.$store.state.token).then(resp => {
            if (resp.success) {
              this.$message({type: 'success', message: '密码修改成功，请重新登录！', showClose: true})
              this.$store.dispatch('logout').then(() => {
                this.$router.push({path: '/'})
              })
            } else {
              this.$message({type: 'warning', message: '密码修改失败，请检查原密码是否输入有误！', showClose: true})
            }
          }).catch(err => {
            this.$message({type: 'warning', message: `密码修改失败，请检查原密码是否输入有误！`, showClose: true})
          })
        } else {
          this.$message({type: 'warning', message: '无法发起请求，请检查输入是否正确！', showClose: true})
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>
