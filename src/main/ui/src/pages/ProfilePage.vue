<template>
  <UserNavigation></UserNavigation>
  <template v-if="user !== null && user !== undefined">
   <h1>{{this.user.fullName}}</h1>
    <h2 v-if="this.user.numberOfRatings && this.user.numberOfRatings > 0">User's rating (average from projects): {{this.user.rating}}/5 (based on {{this.user.numberOfRatings}} grades)</h2>
    <h2 v-else>This user doesn't yet have a rating</h2>
    <h2>Bio:</h2>
    <div>{{this.user.bio}}</div>
    <h2>Interests:</h2>
    <div v-for="tag in this.user.tags" :key="tag.id">
      <div>{{tag.name}}</div>
    </div>
    <h2>Skills:</h2>
    <div v-for="skill in this.user.skills" :key="skill.id">
      <div>{{skill.name}}</div>
    </div>
    <button v-if="this.userId == this.getVisitorUserId()" @click="editProfile" style="font-size: 24px; margin-top: 24px">Edit profile</button>
    <h2>Projects:</h2>
    <div class="custom-table">
      <table>
        <thead>
        <tr>
          <th>Project</th>
          <th>Role</th>
          <th>Total value</th>
          <th>Total contributions</th>
          <th>Rating</th>
          <th>Date of joining</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="unfinishedProjectUserInfo in projects" :key="unfinishedProjectUserInfo.project.id">
          <td>
            <router-link :to="{ name: 'project-page', params: { id: unfinishedProjectUserInfo.project.id} }">
              {{ unfinishedProjectUserInfo.project.name }}
              <span v-if="unfinishedProjectUserInfo.project.isFinished === true"> (Finished)</span>
            </router-link>
          </td>
          <td>{{ unfinishedProjectUserInfo.collaborator.isAdmin === true ? "Admin" : "Collaborator" }}</td>
          <td>{{ unfinishedProjectUserInfo.collaborator.totalValue}}</td>
          <td>{{ unfinishedProjectUserInfo.collaborator.numberOfContributions }}</td>
          <td>{{ unfinishedProjectUserInfo.collaborator.rating }}</td>
          <td>{{ unfinishedProjectUserInfo.collaborator.dayOfJoining }}/{{ unfinishedProjectUserInfo.collaborator.monthOfJoining}}/{{ unfinishedProjectUserInfo.collaborator.yearOfJoining}}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </template>
</template>

<script>
import {
  getProjectsThatUserIn,
} from "@/services/ProjectService";
import {getUserInfo} from "@/services/userService";
import UserNavigation from "@/components/UserNavigation";
export default {
  name: "ProfilePage",
  components: {UserNavigation},
  data() {
    return {
      user: null,
      projects: [],
      userId: null
    }
  },
  props:{
    id:{
      type: String,
      required: true,
    }
  },
  methods: {
    editProfile() {
      this.$router.push({path: '/edit-profile'})
    },
    getVisitorUserId() {
      return localStorage.getItem('userId');
    }
  },
  mounted() {
    console.log("props id ", this.id);
    this.userId = this.id.split('&')[0]
    console.log("fixed props id", this.userId)
    console.log("loacl storage userId", localStorage.getItem('userId'));
    getUserInfo(this.userId).then((response) => {
      if (response) {
        console.log(response)
        this.user = response
        getProjectsThatUserIn(this.userId).then((response) => {
          if (response) {
            console.log(response)
            this.projects = response
          }
        })
      }
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
</style>