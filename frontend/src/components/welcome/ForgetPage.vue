<script setup>

import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})
const validatePassword = (rule, value, callback) => {
  if (value === '')
  {
    callback(new Error('请再次输入密码'))
  }else if (value!== form.password)
  {
    callback(new Error('两次输入的密码不一致!'))
  }
  else {
    callback()
  }
}
const rules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'] }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码长度在6-16个字符', trigger: ['blur','change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur','change'] }]
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 150px">
      <div style=" margin-top: 150px">
        <div style="font-size: 25px">重置密码</div>
      </div>
    </div>
<!--    电子邮件地址-->
    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef" style="margin-top: 60px">
      <el-form-item prop="email">
        <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>
<!--      输入验证码和发送验证码-->
      <div style="margin-top: 10px">
      <el-form-item prop="code">
        <el-row :gutter="10" >
          <el-col :span="17">
            <el-input
                v-model="form.code"
                :maxlength="6"
                type="text"
                placeholder="请输入验证码">
              <template #prefix>
                <el-icon><EditPen /></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="5">
            <el-button
                type="success"
                @click="validateEmail"
                :disabled="!isEmailValid || coldTime > 0">
              {{ coldTime > 0 ? '请稍后 ' + coldTime + ' 秒' : '获取验证码' }}
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
      </div>
    </el-form>
    <div style="margin-top: 20px" >
      <el-form-item prop ="password" rule="rules" model="form" @validate="onValidate" ref="formRef">
        <el-input v-model="form.password" :maxlength="16" type="password" placeholder="新密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
            </template>
          </el-input>
      </el-form-item>
      <el-form-item prop ="password_repeat" rule="rules" model="form" @validate="onValidate" ref="formRef">
        <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="请确认新密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
    </div>
<!--    重置按钮-->
    <div style="margin-top: 70px">
      <el-button style="width: 270px" type="danger" plain>立即重置</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>