<template>
  <UserNavigation></UserNavigation>
  <ProjectAdminNavigation></ProjectAdminNavigation>
  <h2>Pending applications</h2>
  <div style="display: flex; justify-content: center;">
    <ul>
      <li v-for="applicationInfo in pendingApplications" :key="applicationInfo.application.id" style="display: flex; align-items: center;">
        <!--          <router-link :to="{ name: 'user-page', params: { id: application.userId} }">-->
<!--        <a href="#">-->
<!--          {{ applicationInfo.userFullName }}-->
<!--        </a>-->
        <router-link :to="{ name: 'profile-page', params: { id: applicationInfo.application.userId} }">
          {{ applicationInfo.userFullName }}
        </router-link>
        <button @click="approveApplication(applicationInfo.application.id, applicationInfo.application.userId)">Approve</button>
        <button @click="rejectApplication(applicationInfo.application.id)">Reject</button>
      </li>
    </ul>
  </div>
</template>

<script>
import UserNavigation from "@/components/UserNavigation";
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import {approveApplication, getProjectPendingApplications, rejectApplication} from "@/services/ApplicationService";
export default {
  name: "ApplicationsPage",
  components: {UserNavigation, ProjectAdminNavigation},
  data() {
    return {
      pendingApplications: []
    }
  },
  methods: {
    approveApplication(applicationId, userId) {
      approveApplication(applicationId, userId, localStorage.getItem('projectId'), () => {
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
    getProjectPendingApplications(localStorage.getItem('projectId')).then(response => {
      console.log(response)
      this.pendingApplications = response
    })
  }
}
</script>

<style scoped>
button {
  margin-left: 10px;
}
</style>