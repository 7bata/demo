<script setup>
import {Lock, User} from "@element-plus/icons-vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {reactive, onMounted} from "vue";
import "element-plus/es/components/message/style/css";


const form = reactive({
  username: '',
  password: '',
  remember: false
})

// 页面加载时检查是否有保存的用户名
onMounted(() => {
  const savedUsername = localStorage.getItem('rememberedUsername')
  if (savedUsername) {
    form.username = savedUsername
    form.remember = true
  }
})

const login = () => {
  if (!form.username || !form.password) {
    ElMessage.warning('用户名或密码不能为空！')
  }
  else {
    // 如果勾选了记住我，保存用户名到localStorage
    if (form.remember) {
      localStorage.setItem('rememberedUsername', form.username)
    } else {
      localStorage.removeItem('rememberedUsername')
    }
    
    post('/api/auth/login', (message) => {
      ElMessage.success(message)
      router.push('/index')
    },{
          username: form.username,
          password: form.password,
          remember: form.remember
    }, (message)=>{
      ElMessage.warning(message)
    })
  }
}
</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <!-- 最上方登录标题 -->
    <div style=" margin-top: 150px">
      <div style="font-size: 25px">登录</div>
    </div>
    <!-- 登录输入框 -->
    <div style="margin-top: 60px">
      <el-input v-model="form.username" type="text" placeholder="用户名/邮箱">
        <template #prefix>
          <el-icon><User /></el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password" type="password" style="margin-top: 10px" placeholder="密码">
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </div>
    <!-- 记住我&忘记密码 -->
    <div style="margin-top: 3px">
      <el-row>
        <el-col :span="12" style="text-align: left">
          <el-checkbox v-model="form.remember" label="记住我" size="large" />
        </el-col>
        <el-col :span="12" style="text-align: right; margin-top: 7px">
          <el-link @click="router.push('/forget') " href="#">忘记密码/重置密码</el-link>
        </el-col>
      </el-row>
    </div>
    <!-- 登录按钮 -->
    <div style="margin-top: 40px">
      <el-button @click="login()" style="width: 270px" type="success" plain>立即登录</el-button>
    </div>
    <!-- 注册按钮 -->
    <el-divider>
      <span style="color: gray;font-size: 13px;white-space: nowrap">没有账号？请联系管理员获取</span>
    </el-divider>
<!--    <div style="margin-top: 40px">-->
<!--      <el-button style="width: 270px" type="warning" plain>注册账号</el-button>-->
<!--    </div>-->
  </div>
</template>

<style scoped>
</style>