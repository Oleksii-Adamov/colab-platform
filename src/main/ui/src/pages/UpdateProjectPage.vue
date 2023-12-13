<template>
  <UserNavigation></UserNavigation>
  <ProjectAdminNavigation></ProjectAdminNavigation>
  <div id="form-main-container">
    <div id="form-area">
      <button v-if="this.isFinished === false" @click="markAsFinished" style="font-size: 24px; margin-bottom: 20px;">Mark as finished</button>
      <div id="form-title">
        Update project
      </div>
      <div id="form-body">
        <div>
          <div class="col-12">
            <fieldset class="form-group">
              <label class="form-label"><h2>Project name</h2></label>
              <input type="text" class="form-control" v-model="projectName" required>
            </fieldset>
          </div>
          <div class="col-12">
            <fieldset class="form-group">
              <label class="form-label"><h2>Description</h2></label>
              <textarea v-model="projectDescription" rows="5" cols="33"></textarea>
            </fieldset>
          </div>
          <div class="col-12">
            <fieldset class="form-group">
              <div class="checkbox-wrapper-1">
                <label class="form-label"><h2>Project tags</h2></label>
                <div v-for="tag in tags" :key="tag.id">
                  <input style="display: inline-block;" aria-hidden="true" type="checkbox" v-model="selectedTags" :value="tag.id"/>
                  <label style="display: inline-block;">{{tag.name}}</label>
                </div>
              </div>
            </fieldset>
            <fieldset class="form-group">
              <div class="checkbox-wrapper-1">
                <label class="form-label"><h2>Desired skills</h2></label>
                <div v-for="skill in skills" :key="skill.id">
                  <input style="display: inline-block;" aria-hidden="true" type="checkbox" v-model="selectedSkills" :value="skill.id"/>
                  <label style="display: inline-block;">{{skill.name}}</label>
                </div>
              </div>
            </fieldset>
          </div>
        </div>
        <div>
          <div class="center-text button-area">
            <button type="button" class="btn btn-send" @click="submitUpdateProject">Update</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import {getProjectInfo, markAsFinished, updateProject} from "@/services/ProjectService";
import {getTags} from "@/services/tagService";
import {getSkills} from "@/services/skillService";
import UserNavigation from "@/components/UserNavigation";
export default {
  name: "UpdateProjectPage",
  components: {UserNavigation, ProjectAdminNavigation},
  data() {
    return {
      projectName: "",
      projectDescription: "",
      tags: [],
      selectedTags: [],
      skills: [],
      selectedSkills: [],
      isFinished: false
    }
  },
  methods: {
    submitUpdateProject() {
      if (this.projectName !== "") {
        if (localStorage.getItem('projectId')) {
          updateProject(localStorage.getItem('projectId'), this.projectName, this.projectDescription, this.selectedTags, this.selectedSkills, () => {
            this.$router.push({path: '/projects/' + localStorage.getItem('projectId')});
          });
        }
        else {
          alert("Project session expired")
          this.$router.push({path: '/my-projects'});
        }
      } else {
        alert("Project name field must be filled");
      }
    },
    getPossibleTagsAndSkills() {
      getTags().then(response => {
        console.log(response)
        this.tags = response
      })
      getSkills().then(response => {
        console.log(response)
        this.skills = response
      })
    },
    markAsFinished() {
      if (localStorage.getItem('projectId')) {
        markAsFinished(localStorage.getItem('projectId'),
            () => {this.$router.push({path: '/projects/' + localStorage.getItem('projectId')})})
      }
      else {
        alert("Project session expired")
        this.$router.push({path: '/my-projects'});
      }
    }
  },
  mounted() {
    const projectId = localStorage.getItem('projectId')
    if (projectId) {
      getProjectInfo(projectId).then(response => {
        console.log(response)
        const project = response
        this.projectName = project.name
        this.projectDescription = project.description
        this.isFinished = project.isFinished
        for (const tag of project.tags) {
          this.selectedTags.push(tag.id)
        }
        for (const skill of project.skills) {
          this.selectedSkills.push(skill.id)
        }
        this.getPossibleTagsAndSkills();
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
body {
  background-color: #ECEFF4;
}

*,
*:after,
*:before {
  box-sizing: border-box;
}
.center-text{
  text-align:center;
}
#form-main-container {
  display: block;
  position: relative;
  background-color: #fff;
  border-color: #d8e2e7;
  border: 1px solid #e5e5e5;
  border-radius: .25rem;
  margin-top: 2rem;
  margin-bottom: 2rem;
  margin-left: auto;
  margin-right: auto;
  width: 80%;
  padding: 1%;
}

#tabs {
  position: relative;
  width: 100%;
  margin: 0 auto;
  font-weight: 300;
  font-size: 1.5rem;
  text-align: center;
}

#tabs ul {
  position: relative;
  display: -ms-flexbox;
  display: -webkit-flex;
  display: -moz-flex;
  display: -ms-flex;
  display: -webkit-box;
  display: flex;
  margin: 0 auto;
  padding: 0;
  max-width: 90%;
  list-style: none;
  -webkit-box-orient: horizontal;
  -webkit-flex-flow: row wrap;
  -ms-flex-flow: row wrap;
  flex-flow: row wrap;
  -webkit-justify-content: center;
  -moz-justify-content: center;
  -ms-justify-content: center;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
}

#tabs ul li {
  position: relative;
  display: block;
  width: 100%;
  margin: 0.5%;
  padding: 1%;
  text-align: center;
  flex: 1;
  z-index: 1;
  border-radius: 2%;
}

#tabs ul li a {
  position: relative;
  display: block;
  color: #343434;
  overflow: visible;
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  transition: border 0.5s;
  white-space: nowrap;
  line-height: 2.5;
  text-decoration: none;
  outline: none;
}

#tabs ul li a:hover {
  border-bottom: 1px solid rgba(0, 0, 0, 1);
  transition: border 0.5s;
}

#tabs ul li.active-tab a:before {
  position: absolute;
  top: 100%;
  left: 50%;
  width: 0;
  height: 0;
  border: solid transparent;
  content: '';
  pointer-events: none;
  border-width: 1rem;
  border-top-color: rgba(0, 0, 0, 0.2);
  margin-left: -1rem;
  transition: border 0.5s;
}

#tabs ul li.active-tab a:hover:before {
  border-top-color: rgba(0, 0, 0, 0.5);
  transition: border 0.5s;
}

#tabs ul li.active-tab a:after {
  position: absolute;
  top: 100%;
  left: 50%;
  width: 0;
  height: 0;
  border: solid transparent;
  content: '';
  pointer-events: none;
  border-width: 0.9rem;
  border-top-color: #fff;
  margin-left: -0.9rem;
}

#form-area {
  position: relative;
  overflow: hidden;
  width: 100%;
  font-weight: 300;
  font-size: 1.2rem;
  margin: 1rem;
  margin-top: 2rem;
}

#form-title {
  border-bottom: solid 1px #d8e2e7;
  padding-bottom: .8rem;
  width: 97%;
  font-weight:600;
  /*color:#8120a1;*/
  color:#777;
}

#form-body {
  margin-top: 2rem;
}

.col-12 {
  display: inline-block;
  width: 95%;
  margin: 1%;
  padding: 1%;
}


.button-area {
  margin-top: 1%;
  margin-right: 5%;
  margin-left: 1%;
  padding: 2%;
}

.form-group {
  margin-bottom: 0.2rem;
}

fieldset {
  border: 0;
  padding: 0;
}

.form-label {
  display: block;
  margin-bottom: 6px;
  font-size: 1rem;
}

label {
  margin: 0;
  display: block;
}

.form-control {
  box-shadow: none;
  font-size: 1rem;
  color: #343434!important;
  width: 100%;
  padding: .375rem .75rem;
  line-height: 1.5;
  background-color: #fff;
  background-image: none;
  border: 1px solid #ccc;
  -webkit-border-radius: .25rem;
  border-radius: .25rem;
}

textarea {
  resize: none;
  border: 0.1rem solid #ccc;
  border-radius: 0.25rem;
  width: 100%;
}

.btn {
  -webkit-border-radius: 3px;
  border-radius: 3px;
  border: 1px solid #00a8ff;
  background: #00a8ff;
  color: #fff;
  font-weight: 600;
  display: inline-block;
  padding: .375rem 1rem;
  font-size: 1rem;
  line-height: 1.5;
  text-align: center;
  vertical-align: middle;
  cursor: pointer;
  user-select: none;
}

.btn-cancel {
  background-color: #8120A1;
  border-color: #8120A1;
}

.btn-send {
  background-color: #6b7a85;
  border-color: #6b7a85;
}

.btn-save {
  background-color: #6b7a85;
  border-color: #6b7a85;
}
label{
  color:#777;
  font-weight:600;
}
.form-control{
  color:#777 !important;
}

.checkbox-wrapper-1 *,
.checkbox-wrapper-1 ::after,
.checkbox-wrapper-1 ::before {box-sizing: border-box;
}
.checkbox-wrapper-1 [type=checkbox].substituted {
  margin: 0;
  width: 0;
  height: 0;
  display: inline;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}
.checkbox-wrapper-1 [type=checkbox].substituted + label:before {
  content: "";
  display: inline-block;
  vertical-align: top;
  height: 1.15em;
  width: 1.15em;
  margin-right: 0.6em;
  color: rgba(0, 0, 0, 0.275);
  border: solid 0.06em;
  box-shadow: 0 0 0.04em, 0 0.06em 0.16em -0.03em inset, 0 0 0 0.07em transparent inset;
  border-radius: 0.2em;
  background: url('data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" version="1.1" xml:space="preserve" fill="white" viewBox="0 0 9 9"><rect x="0" y="4.3" transform="matrix(-0.707 -0.7072 0.7072 -0.707 0.5891 10.4702)" width="4.3" height="1.6" /><rect x="2.2" y="2.9" transform="matrix(-0.7071 0.7071 -0.7071 -0.7071 12.1877 2.9833)" width="6.1" height="1.7" /></svg>') no-repeat center, white;
  background-size: 0;
  will-change: color, border, background, background-size, box-shadow;
  transform: translate3d(0, 0, 0);
  transition: color 0.1s, border 0.1s, background 0.15s, box-shadow 0.1s;
}
.checkbox-wrapper-1 [type=checkbox].substituted:enabled:active + label:before,
.checkbox-wrapper-1 [type=checkbox].substituted:enabled + label:active:before {
  box-shadow: 0 0 0.04em, 0 0.06em 0.16em -0.03em transparent inset, 0 0 0 0.07em rgba(0, 0, 0, 0.1) inset;
  background-color: #f0f0f0;
}
.checkbox-wrapper-1 [type=checkbox].substituted:checked + label:before {
  background-color: #3B99FC;
  background-size: 0.75em;
  color: rgba(0, 0, 0, 0.075);
}
.checkbox-wrapper-1 [type=checkbox].substituted:checked:enabled:active + label:before,
.checkbox-wrapper-1 [type=checkbox].substituted:checked:enabled + label:active:before {
  background-color: #0a7ffb;
  color: rgba(0, 0, 0, 0.275);
}
.checkbox-wrapper-1 [type=checkbox].substituted:focus + label:before {
  box-shadow: 0 0 0.04em, 0 0.06em 0.16em -0.03em transparent inset, 0 0 0 0.07em rgba(0, 0, 0, 0.1) inset, 0 0 0 3.3px rgba(65, 159, 255, 0.55), 0 0 0 5px rgba(65, 159, 255, 0.3);
}
.checkbox-wrapper-1 [type=checkbox].substituted:focus:active + label:before,
.checkbox-wrapper-1 [type=checkbox].substituted:focus + label:active:before {
  box-shadow: 0 0 0.04em, 0 0.06em 0.16em -0.03em transparent inset, 0 0 0 0.07em rgba(0, 0, 0, 0.1) inset, 0 0 0 3.3px rgba(65, 159, 255, 0.55), 0 0 0 5px rgba(65, 159, 255, 0.3);
}
.checkbox-wrapper-1 [type=checkbox].substituted:disabled + label:before {
  opacity: 0.5;
}

.checkbox-wrapper-1 [type=checkbox].substituted.dark + label:before {
  color: rgba(255, 255, 255, 0.275);
  background-color: #222;
  background-image: url('data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" version="1.1" xml:space="preserve" fill="rgba(34, 34, 34, 0.999)" viewBox="0 0 9 9"><rect x="0" y="4.3" transform="matrix(-0.707 -0.7072 0.7072 -0.707 0.5891 10.4702)" width="4.3" height="1.6" /><rect x="2.2" y="2.9" transform="matrix(-0.7071 0.7071 -0.7071 -0.7071 12.1877 2.9833)" width="6.1" height="1.7" /></svg>');
}
.checkbox-wrapper-1 [type=checkbox].substituted.dark:enabled:active + label:before,
.checkbox-wrapper-1 [type=checkbox].substituted.dark:enabled + label:active:before {
  background-color: #444;
  box-shadow: 0 0 0.04em, 0 0.06em 0.16em -0.03em transparent inset, 0 0 0 0.07em rgba(255, 255, 255, 0.1) inset;
}
.checkbox-wrapper-1 [type=checkbox].substituted.dark:checked + label:before {
  background-color: #a97035;
  color: rgba(255, 255, 255, 0.075);
}
.checkbox-wrapper-1 [type=checkbox].substituted.dark:checked:enabled:active + label:before,
.checkbox-wrapper-1 [type=checkbox].substituted.dark:checked:enabled + label:active:before {
  background-color: #c68035;
  color: rgba(0, 0, 0, 0.275);
}
</style>