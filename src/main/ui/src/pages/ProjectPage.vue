<template>
  <div>
<!--    <h1>{{this.name}}</h1>-->
    <template v-if="project !== null">
      <h1>{{this.project.name}}</h1>
      <h2>Description</h2>
      <div>{{this.project.projectDescription}}</div>
      <h2>Tags:</h2>
      <div v-for="tag in this.project.tags" :key="tag.id">
        <div>{{tag.name}}</div>
      </div>
      <h2>Skills:</h2>
      <div v-for="skill in this.project.skills" :key="skill.id">
        <div>{{skill.name}}</div>
      </div>
      <h2>Contributions:</h2>
      <ul>
        <li v-for="item in this.contributions" :key="item.userId">
        </li>
      </ul>
    </template>
  </div>
</template>

<script>

import {getProjectInfo} from "@/services/ProjectService";

export default {
  name: "ProjectPage",
  components: {

  },
  data() {
    return {
      // name: "",
      project: null,
      contributions: []
    }
  },
  props:{
    id:{
      //type: Number,
      type: String,
      required: true,
    }
  },
  mounted() {
    console.log("props id ", this.id);
    getProjectInfo(this.id).then(response => {
      console.log(response)
      this.project = response
    });
    // this.name = this.$route.params.name;
  }
}
</script>
<style scoped>

</style>