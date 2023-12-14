<template>
  <UserNavigation></UserNavigation>
  <ProjectAdminNavigation></ProjectAdminNavigation>
  <div class="custom-table">
    <h2>Collaborators</h2>
    <table>
      <thead>
      <tr>
        <th>User</th>
        <th>Role</th>
        <th>Total value</th>
        <th>Total contributions</th>
        <th>Rate</th>
        <th>Date of joining</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="collaboratorInfo in collaborators" :key="contributionInfo.contribution.id">
        <td>
          <router-link :to="{ name: 'profile-page', params: { id: collaboratorInfo.contribution.userId} }">
            {{ collaboratorInfo.userFullName }}
          </router-link>
        </td>
        <td>
          <div v-if="collaboratorInfo.collaborator.isAdmin === true">Admin</div>
          <div v-else>
            Collaborator
            <button @click="makeAdmin(collaboratorInfo.collaborator.id)">Make Admin</button>
          </div>
          {{ collaboratorInfo.collaborator.isAdmin === true ? "Admin" : "Collaborator" }}
        </td>
        <td>{{ collaboratorInfo.collaborator.totalValue}}</td>
        <td>{{ collaboratorInfo.collaborator.numberOfContributions }}</td>
<!--        <td>{{ collaboratorInfo.collaborator.rating }}</td>-->
        <td>
          <div class="star-rating">
            <span class="star" v-for="(star, index) in 5" :key="index" @click="rateCollaborator(collaboratorInfo.collaborator.id, index + 1)" :class="{ 'filled': index < collaboratorInfo.collaborator.rating }">&#9733;</span>
          </div>
        </td>
        <td>{{ collaboratorInfo.collaborator.dayOfJoining }}/{{ unfinishedProjectUserInfo.collaborator.monthOfJoining}}/{{ unfinishedProjectUserInfo.collaborator.yearOfJoining}}</td>
      </tr>

      </tbody>
    </table>
  </div>
</template>

<script>
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import UserNavigation from "@/components/UserNavigation";
import {getUserInfo} from "@/services/userService";
import {getProjectsThatUserIn} from "@/services/ProjectService";

export default {
  name: "CollaboratorsPage",
  components: {
    ProjectAdminNavigation,
    UserNavigation
  },
  data() {
    return {
      collaborators: []
    }
  },
  methods: {
    makeAdmin(id) {
      // make admin
    },
    rateCollaborator(id, rating) {
      // rate
    }
  },
  mounted() {
    const projectId = localStorage.getItem('projectId');
    if (projectId) {
      getProjectCollaborators(projectId).then(response => {
        console.log(response)
        this.pendingContributions = response
      })
    }
    else {
      alert("Project session expired")
      this.$router.push({path: '/my-projects'});
    }
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