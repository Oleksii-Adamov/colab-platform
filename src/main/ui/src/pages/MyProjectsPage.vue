<template>
  <UserNavigation></UserNavigation>
  <div>
        <h2>Projects you are in</h2>
        <ul>
          <li v-for="item in projects" :key="item.project.id">
            <router-link :to="{ name: 'project-page', params: { id: item.project.id} }">
              {{ item.project.name }}
              <span v-if="item.project.isFinished === true"> (Finished)</span>
              <span v-if="item.isAdmin === true" style="color: red;"> | Administrating</span>
            </router-link>
          </li>
        </ul>
  </div>
</template>

<script>
import {getProjectsThatUserIn} from "@/services/ProjectService";
import UserNavigation from "@/components/UserNavigation";

export default {
  name: "ProjectsPage",
  components: {
    UserNavigation
  },
  data() {
    return {
      projects: []
    }
  },
  methods: {
    getProjects() {
      getProjectsThatUserIn().then(response => {
        console.log(response)
        this.projects = response
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