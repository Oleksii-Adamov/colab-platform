<template>
  <UserNavigation></UserNavigation>
  <ProjectAdminNavigation></ProjectAdminNavigation>
  <div class="custom-table">
    <h2>Pending contributions</h2>
    <table>
      <thead>
      <tr>
        <th class="description-column">Description</th>
        <th>Value</th>
        <th>Date</th>
        <th>User</th>
        <th>Approve</th>
        <th>Reject</th>
      </tr>
      </thead>
      <tbody>
        <tr v-for="contributionInfo in pendingContributions" :key="contributionInfo.contribution.id">
          <td>{{ contributionInfo.contribution.description }}</td>
          <td>{{ contributionInfo.contribution.value }}</td>
          <td>{{ contributionInfo.contribution.day }}/{{ contributionInfo.contribution.month}}/{{ contributionInfo.contribution.year}}</td>
          <td>{{ contributionInfo.userFullName }}</td>
          <td><button @click="approveContribution(contributionInfo.contribution.id)">Approve</button></td>
          <td><button @click="rejectContribution(contributionInfo.contribution.id)">Reject</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import UserNavigation from "@/components/UserNavigation";
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import {approveContribution, getProjectPendingContributions, rejectContribution} from "@/services/contributionService";

export default {
  name: "PendingContributionsPage",
  components: {UserNavigation, ProjectAdminNavigation},
  data() {
    return {
      pendingContributions: []
    }
  },
  methods: {
    approveContribution(contributionId) {
      approveContribution(contributionId, () => {
        this.removeFromPendingContributions(contributionId)
      })
    },
    rejectContribution(contributionId) {
      rejectContribution(contributionId, () => {
        this.removeFromPendingContributions(contributionId)
      })
    },
    removeFromPendingContributions(contributionId) {
      this.pendingContributions = this.pendingContributions.filter(contributionInfo =>
          contributionInfo.contribution.id !== contributionId);
    }
  },
  mounted() {
    getProjectPendingContributions(localStorage.getItem('projectId')).then(response => {
      console.log(response)
      this.pendingContributions = response
    })
  }
}
</script>
<style scoped>
.custom-table {
  width: 80%;
  margin: 0 auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}
.description-column {
  width: 60%;
}
</style>