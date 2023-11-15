<template>
  <UserNavigation></UserNavigation>
  <div>
    <template v-if="project !== null && project !== undefined">
      <h1>{{this.project.name}}</h1>
      <template v-if="is_collaborator === false">
        <button @click="apply">Apply</button>
        <p v-if="applied === true" style="color: green;">Applied</p>
      </template>
      <button v-if="is_collaborator === true" @click="contribute">Contribute</button>
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

import {getProjectInfo, authToProject} from "@/services/ProjectService";
import {createApplication} from "@/services/ApplicationService"
import UserNavigation from "@/components/UserNavigation";

export default {
  name: "ProjectPage",
  components: {
    UserNavigation
  },
  data() {
    return {
      project: null,
      contributions: [],
      collaborator: null,
      is_collaborator: null,
      applied: false
    }
  },
  props:{
    id:{
      type: String,
      required: true,
    }
  },
  methods: {
    apply() {
      createApplication(this.id).then(response => {
        console.log(response)
        this.applied = true
      })
    },
    contribute() {
      console.log("contribute")
    }
  },
  mounted() {
    console.log("props id ", this.id);

    authToProject(this.id).then((response) => {console.log(response)
      if (response.id === undefined) {
        this.is_collaborator = false
      }
      else {
        this.collaborator = response
        this.is_collaborator = true
      }
      getProjectInfo(this.id).then(response => {
        console.log(response)
        this.project = response
      });
    }
    )
  }
}
</script>
<style scoped>

</style>