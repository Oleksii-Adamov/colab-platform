import getRequest from "@/services/getRequest";
import postRequest from "@/services/postRequest";

export async function getUsersProjects() {
    return getRequest('/api/projects/user-projects');
}

export async function getProjectsThatUserIn() {
    return getRequest('/api/projects/projects-user-in');
}

export async function getProjectContributions(projectId) {
    return getRequest('/api/projects/contributions?projectId='+projectId);
}

export async function createProject(projectName, projectDescription, selectedTags, selectedSkills, action) {
    return postRequest('/api/projects/create?name=' + projectName + '&userId=' + localStorage.getItem('userId'),
        {'projectDescription': projectDescription, 'selectedTags': selectedTags,
        'selectedSkills': selectedSkills}, action);
}
