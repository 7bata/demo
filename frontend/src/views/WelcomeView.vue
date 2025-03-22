<script>

</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1; background-color: dodgerblue">
      <!-- 左侧背景 需要添加图片 -->
      <!--      <el-image>-->

      <!--      </el-image>-->
    </div>
    <!-- 欢迎语 -->
    <div class="welcome-content">
      <div style="font-size: 30px; font-weight: bold;">
        This is the welcome page of Educational Management System for Osmosis!
      </div>
      <div>
        (Need to be replaced with real content)
      </div>
    </div>
    <!-- 右侧背景 登录板块 -->
    <div style="width: 400px; background-color: white; ">
<!--      不同板块之间的切换-->
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.welcome-content{
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>