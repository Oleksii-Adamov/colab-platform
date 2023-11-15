<template>
  <UserNavigation></UserNavigation>
  <div>
    <h2>Projects</h2>
    <ul>
      <li v-for="item in projects" :key="item.id">
        <router-link :to="{ name: 'project-page', params: { id: item.id} }">
          {{ item.name }}
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import {getRecommendedProjects} from "@/services/ProjectService";
import UserNavigation from "@/components/UserNavigation";

export default {
  name: "ProjectSearchPage",
  components: {
    UserNavigation
  },
  data() {
    return {
      projects: []
    }
  },
  methods: {
    getRecommendedProjects() {
      getRecommendedProjects().then(response => {
        this.projects = response
      });
    },
  },
  mounted() {
    this.getRecommendedProjects();
  }
}
</script>

<style scoped>

</style>