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
      <tr v-for="collaboratorInfo in collaborators" :key="collaboratorInfo.collaborator.id">
        <td>
          <router-link :to="{ name: 'profile-page', params: { id: collaboratorInfo.collaborator.userId} }">
            {{ collaboratorInfo.userFullName }}
          </router-link>
        </td>
        <td>
          <div v-if="collaboratorInfo.collaborator.isAdmin === true">Admin</div>
          <div v-else>
            Collaborator
            <button @click="makeAdmin(collaboratorInfo.collaborator.id)">Make Admin</button>
          </div>
        </td>
        <td>{{ collaboratorInfo.collaborator.totalValue}}</td>
        <td>{{ collaboratorInfo.collaborator.numberOfContributions }}</td>
<!--        <td>{{ collaboratorInfo.collaborator.rating }}</td>-->
        <td>
          <div class="star-rating">
            <span class="star" v-for="(star, index) in 5" :key="index" @click="rateCollaborator(collaboratorInfo.collaborator.id, index + 1)" :class="{ 'filled': index < collaboratorInfo.collaborator.rating }">&#9733;</span>
          </div>
        </td>
        <td>{{ collaboratorInfo.collaborator.dayOfJoining }}/{{ collaboratorInfo.collaborator.monthOfJoining}}/{{ collaboratorInfo.collaborator.yearOfJoining}}</td>
      </tr>

      </tbody>
    </table>
  </div>
</template>

<script>
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import UserNavigation from "@/components/UserNavigation";
import {getProjectCollaborators, makeAdmin, rateCollaborator} from "@/services/ProjectService";

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
      // make collaborator.isAdmin = true for collaborators.collaborator.id = id
      const collaboratorToUpdate = this.collaborators.find(collaboratorInfo => collaboratorInfo.collaborator.id === id);
      if (collaboratorToUpdate) {
        collaboratorToUpdate.collaborator.isAdmin = true;
      } else {
        console.error('Collaborator not found.');
      }
      makeAdmin(id, (response) => {
        console.log(response)
      })
    },
    rateCollaborator(id, rating) {
      const collaboratorToUpdate = this.collaborators.find(collaboratorInfo => collaboratorInfo.collaborator.id === id);
      if (collaboratorToUpdate) {
        collaboratorToUpdate.collaborator.rating = rating;
      } else {
        console.error('Collaborator not found.');
      }
      rateCollaborator(id, rating, (response) => {
        console.log(response)
      })
    }
  },
  mounted() {
    const projectId = localStorage.getItem('projectId');
    if (projectId) {
      getProjectCollaborators(projectId).then(response => {
        console.log(response)
        this.collaborators = response
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