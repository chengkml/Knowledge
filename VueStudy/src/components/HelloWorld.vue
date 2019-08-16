<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div class="hello">
    <ol>
      <li>
        <p>使用“{{}}”模板语法</p>
        <div>
          {{templateStr}}
        </div>
      </li>
      <li>
        <p>v-bind实例</p>
        <span v-bind:title="spanTitle">鼠标悬停有惊喜哦！</span>
      </li>
    </ol>
    <ol>
      <li  style="float:right;">a</li>
      <li>a</li>
      <li>a</li>
    </ol>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      templateStr: 'Hellfo,CK!',
      spanTitle:'页面加载于'+new Date().toLocaleString()
    }
  },
  methods:{
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
