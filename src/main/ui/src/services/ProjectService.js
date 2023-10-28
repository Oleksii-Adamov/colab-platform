import getRequest from "@/services/getRequest";

export async function getUsersProjects() {
    return getRequest('/api/projects/user-projects');
}
export async function getProjectsThatUserIn() {
    return getRequest('/api/projects/projects-user-in');
}

export async function getProjectContributions(projectId) {
    return getRequest('/api/projects/contributions?projectId='+projectId);
}
