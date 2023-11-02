<template>
  <div>
    <div>
      <div>
        <h2>Your projects</h2>
        <ul>
          <li v-for="item in your_projects" :key="item.id">
            <router-link :to="{ name: 'project-page', params: { id: item.id, name: item.name} }"> {{ item.name }} </router-link>
          </li>
        </ul>
        <h2>Projects you are in</h2>
        <ul>
          <li v-for="item in in_projects" :key="item.id">
            <router-link :to="{ name: 'project-page', params: { id: item.id, name: item.name} }"> {{ item.name }} </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import {getProjectsThatUserIn, getUsersProjects} from "@/services/ProjectService";

export default {
  name: "ProjectsPage",
  components: {},
  data() {
    return {
      your_projects: [],
      in_projects: []
    }
  },
  methods: {
    getProjects() {
      getUsersProjects().then(response => {
        console.log(response)
        this.your_projects = response
      });
      getProjectsThatUserIn().then(response => {
        console.log(response)
        this.in_projects = response
      });
    },
  },
  mounted() {
    this.getProjects();
  }
}
</script>

<style scoped>

</style>