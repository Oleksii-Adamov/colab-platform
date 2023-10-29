<template>
  <div>
    <label>Project name</label>
    <input v-model="projectName">
    <button @click="submitCreateProject">Create</button>
  </div>
</template>

<script>
import {createProject} from "@/services/ProjectService";

export default {
  name: "CreateCoursePage",
  data() {
    return {
      projectName: ""
    }
  },
  methods: {
    submitCreateProject() {
      if (this.projectName !== "") {
        createProject(this.projectName, () => {
          this.$router.push({path: '/my-projects'});
        });
      } else {
        alert("All field must be field");
      }
    }
  }
}
</script>

<style scoped>

</style>