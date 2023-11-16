<template>
  <UserNavigation></UserNavigation>
  <template v-if="project !== null && project !== undefined">
<!--    <ProjectAdminNavigation v-if="collaborator !== null && collaborator.isAdmin === true"></ProjectAdminNavigation>-->
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
    <div v-if="pendingApplications.length > 0">
      <h2>Pending applications</h2>
      <div style="display: flex; justify-content: center;">
        <ul>
          <li v-for="applicationInfo in pendingApplications" :key="applicationInfo.application.id" style="display: flex; align-items: center;">
  <!--          <router-link :to="{ name: 'user-page', params: { id: application.userId} }">-->
            <a href="#">
              {{ applicationInfo.userFullName }}
            </a>
            <button @click="approveApplication(applicationInfo.application.id, applicationInfo.application.userId)">Approve</button>
            <button @click="rejectApplication(applicationInfo.application.id)">Reject</button>
          </li>
        </ul>
      </div>
    </div>
    <h2>Contributions:</h2>
    <ul>
      <li v-for="item in this.contributions" :key="item.userId">
      </li>
    </ul>
  </template>
</template>

<script>

import {getProjectInfo, authToProject} from "@/services/ProjectService";
import {
  approveApplication,
  createApplication,
  getProjectPendingApplications,
  rejectApplication
} from "@/services/ApplicationService"
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
      applied: false,
      pendingApplications: []
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
      createApplication(this.id, () => {this.applied = true})
    },
    contribute() {
      console.log("contribute")
    },
    approveApplication(applicationId, userId) {
      approveApplication(applicationId, userId, this.id, () => {
        this.removeFromPendingApplications(applicationId)
      })
    },
    rejectApplication(applicationId) {
      rejectApplication(applicationId, () => {
        this.removeFromPendingApplications(applicationId)
      })
    },
    removeFromPendingApplications(applicationId) {
      this.pendingApplications = this.pendingApplications.filter(applicationInfo =>
          applicationInfo.application.id !== applicationId);
    }
  },
  mounted() {
    console.log("props id ", this.id);

    authToProject(this.id).then((response) => {console.log(response)
      if (response === undefined || response.id === undefined) {
        this.is_collaborator = false
      }
      else {
        this.collaborator = response
        this.is_collaborator = true
      }
      getProjectInfo(this.id).then(response => {
        console.log(response)
        this.project = response
        if (this.is_collaborator && this.collaborator.isAdmin === true) {
          getProjectPendingApplications(this.id).then(response => {
            console.log(response)
            this.pendingApplications = response
          })
        }
      });
    }
    )
  }
}
</script>
<style scoped>

</style>