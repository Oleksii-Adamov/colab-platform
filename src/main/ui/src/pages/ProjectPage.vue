<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<template>
  <UserNavigation></UserNavigation>
  <ProjectAdminNavigation v-if="is_admin === true"></ProjectAdminNavigation>
  <template v-if="project !== null && project !== undefined">
    <!--    <ProjectAdminNavigation v-if="collaborator !== null && collaborator.isAdmin === true"></ProjectAdminNavigation>-->
    <h1>{{this.project.name}}</h1>
    <h2 v-if="this.project.numberOfRatings && this.project.numberOfRatings > 0">Project rating (average from user's): {{this.project.rating}}/5 (based on {{this.project.numberOfRatings}} votes)</h2>
    <h2 v-else>This project doesn't yet have rating</h2>
    <div style="margin-bottom: 20px">
      <h2 style="display: inline">Rate project: </h2>
      <div class="star-rating">
        <span class="star" v-for="(star, index) in 5" :key="index" @click="rateProject(index + 1)" :class="{ 'filled': index < selectedStars }">&#9733;</span>
      </div>
    </div>
    <template v-if="is_collaborator === false">
      <button @click="apply">Apply</button>
      <p v-if="applied === true" style="color: green;">Applied</p>
    </template>
    <button v-if="is_collaborator === true" @click="contribute">Contribute</button>
<!--    <router-link v-if="is_collaborator === true" :to="{ path: '/contribute?projectId=' + this.id}"> Contribute</router-link>-->
<!--    <router-link v-if="is_collaborator === true" :to="{ name: 'contribute-page', params: {projectId: this.project.id} }"> Contribute</router-link>-->
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
    <div class="custom-table">
      <table>
        <thead>
        <tr>
          <th class="description-column">Description</th>
          <th>Value</th>
          <th>Date</th>
          <th>User</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="contributionInfo in contributions" :key="contributionInfo.contribution.id">
          <td>{{ contributionInfo.contribution.description }}</td>
          <td>{{ contributionInfo.contribution.value }}</td>
          <td>{{ contributionInfo.contribution.day }}/{{ contributionInfo.contribution.month}}/{{ contributionInfo.contribution.year}}</td>
          <td>{{ contributionInfo.userFullName }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </template>
</template>

<script>

import {getProjectInfo, authToProject, rateProject} from "@/services/ProjectService";
import {
  approveApplication,
  createApplication,
  rejectApplication
} from "@/services/ApplicationService"
import UserNavigation from "@/components/UserNavigation";
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import {getProjectApprovedContributions} from "@/services/contributionService";

export default {
  name: "ProjectPage",
  components: {
    ProjectAdminNavigation,
    UserNavigation
  },
  data() {
    return {
      project: null,
      contributions: [],
      collaborator: null,
      is_collaborator: null,
      is_admin: null,
      applied: false,
      selectedStars: 0
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
      this.$router.push({path: '/contribute?projectId=' + this.id})
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
    },
    rateProject(stars) {
      this.selectedStars = stars;
      rateProject(this.id, this.selectedStars, () => {
        console.log('got rate response')
        getProjectInfo(this.id).then(response => {
          console.log(response)
          this.project = response
        })
      })

    }
  },
  mounted() {
    console.log("props id ", this.id);
    localStorage.setItem('projectId', this.id)
    authToProject(this.id).then((response) => { console.log(response)
      if (response === undefined || response.id === undefined) {
        this.is_collaborator = false
      }
      else {
        this.collaborator = response
        this.is_collaborator = true
      }
      this.is_admin = this.collaborator.isAdmin
      getProjectInfo(this.id).then(response => {
        console.log(response)
        this.project = response
      });
      getProjectApprovedContributions(this.id).then(response => {
        console.log(response)
        this.contributions = response
      });
    }
    )
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
.star-rating {
  font-size: 24px;
  display: inline-block;
}

.star {
  cursor: pointer;
  color: #ccc;
}

.star.filled {
  color: #ffdd00;
}
</style>