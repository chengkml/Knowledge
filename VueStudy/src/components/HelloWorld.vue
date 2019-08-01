<template>
  <div class="hello">
    <el-button @click="testMysql">fd</el-button>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Hellfo,CK!'
    }
  },
  methods:{
    testMysql:function(){
      var mysql      = require('mysql');
      console.log(mysql);
      var connection = mysql.createConnection({
        host     : 'localhost',
        user     : 'root',
        password : 'mysql',
        database : 'zhejiang_dacp'
      });

      connection.connect();

      connection.query('select count(*) from dacp_meta_tab', function (error, results, fields) {
        if (error) throw error;
        console.log('The solution is: ', results[0].solution);
      });

      connection.end();
    }
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
