<script setup lang="js">
import {OpenAPI, UserService} from "@/api";
import router from "@/router";


let password = defineModel("password")
let email = defineModel("email")

function login() {
  UserService.postUserLogin({requestBody: {email: email.value.toString(), password: password.value.toString()}}).then(data => {
    OpenAPI.TOKEN = data.token
    sessionStorage.setItem("token", data.token)
    router.push({path: "/user"})
  }).catch(err => {
    alert("Can't login")
  })
}

</script>

<template>
  <h1>Login</h1>
  <label>Email</label><br>
  <input v-model="email" type="email"/><br>
  <label>Password</label><br>
  <input v-model="password" type="password"/><br>
  <br>
  <button v-on:click="login()">Submit</button>
</template>
