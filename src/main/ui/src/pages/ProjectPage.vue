<template>
  <div>
    <h1>{{this.name}}</h1>
    <h2>Contributions:</h2>
    <ul>
      <li v-for="item in this.contributions" :key="item.userId">
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: "ProjectPage",
  components: {

  },
  data() {
    return {
      name: "",
      contributions: []
    }
  },
  props:{
    id:{
      type: Number,
      required: true,
    }
  },
  mounted() {
    console.log("props id ", this.id);
    this.name = this.$route.params.name;
  }
}
</script>
<style scoped>

</style>