<script setup>
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import router from "@/router";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const formRef = ref(null)
const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

// 邮箱验证状态
const isEmailValid = ref(false)
const coldTime = ref(0)

// 验证密码一致性
const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'] }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码长度在6-16个字符', trigger: ['blur', 'change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change'] }
  ]
}

// 验证邮箱并发送验证码
const validateEmail = () => {
  formRef.value.validateField('email', valid => {
    if (valid) {
      coldTime.value = 60
      const timer = setInterval(() => {
        coldTime.value--
        if (coldTime.value <= 0) {
          clearInterval(timer)
        }
      }, 1000)
      // 发送验证码请求
      post('/api/auth/forget-password/email', (message) => {
        ElMessage.success(message)
      }, {
        email: form.email
      })
    }
  })
}

// 表单验证回调
const onValidate = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValid.value = isValid
  }
}

// 提交重置密码请求
const resetPassword = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/forget-password/reset', (message) => {
        ElMessage.success(message)
        router.push('/')
      }, {
        email: form.email,
        code: form.code,
        password: form.password
      })
    }
  })
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="margin-top: 150px">
        <div style="font-size: 25px">重置密码</div>
      </div>
    </div>
    <!-- 将所有表单项放在同一个el-form内 -->
    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef" style="margin-top: 60px">
      <el-form-item prop="email">
        <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <!-- 输入验证码和发送验证码 -->
      <div style="margin-top: 10px">
        <el-form-item prop="code">
          <el-row :gutter="10">
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
            <el-col :span="7">
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
      
      <!-- 密码和确认密码 -->
      <el-form-item prop="password">
        <el-input v-model="form.password" :maxlength="16" type="password" placeholder="新密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password_repeat">
        <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="请确认新密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      
      <!-- 返回登录链接 -->
      <div style="text-align: left; margin-top: 20px">
        <el-link @click="router.push('/')" type="default">返回登录</el-link>
      </div>
      
      <!-- 重置按钮 -->
      <div style="margin-top: 70px">
        <el-button style="width: 270px" type="danger" plain @click="resetPassword">立即重置</el-button>
      </div>
    </el-form>
  </div>
</template>

<style scoped>
</style>