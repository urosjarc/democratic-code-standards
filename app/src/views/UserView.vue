<script setup lang="js">
import {LanguageService, OpenAPI, UserService} from "@/api";

const user_id = defineModel("user_id")
const lang = [
  {name: "asdf"},
  {name: "asdfsadfas"},
  {name: "ads"},
]

UserService.getUser().then(data => {
  user_id.value = data.user_id
}).catch(err => {
  alert("Error: " + err.toString())
})

LanguageService.getLanguage({}).then(data => {
  languages.value = data
})

let languages = defineModel("languages")

</script>

<template>
  <h1>User</h1>
  <h2>Token {{OpenAPI.TOKEN.slice(0, 10)}}...</h2>
  <h2>User id: {{user_id}}</h2>

  <button style="height: 200px; width: 200px; margin: 20px" v-for="ele in languages" v-on:click="language(ele.id)">{{ele.name}}</button>
</template>
