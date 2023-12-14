import getRequest from "@/services/getRequest";
import postRequest from "@/services/postRequest";

export async function getUsersProjects() {
    return getRequest('/api/projects/user-projects?userId=' + localStorage.getItem('userId'));
}

export async function getProjectsThatUserIn() {
    return getRequest('/api/projects/projects-user-in?userId=' + localStorage.getItem('userId'));
}

export async function getRecommendedProjects() {
    return getRequest('/api/projects/project-recommendations?userId=' + localStorage.getItem('userId'));
}

export async function getProjectInfo(projectId) {
    return getRequest('/api/projects/project-info?projectId='+projectId);
}

export async function authToProject(projectId) {
    return getRequest('/api/projects/auth?userId=' + localStorage.getItem('userId') +
        '&projectId='+projectId);
}
export async function getUserRatingOfProject(projectId) {
    return getRequest('/api/projects/user-rating?projectId=' + projectId +
        '&userId='+ localStorage.getItem('userId'));
}

export async function getProjectStats(projectId, beginningMonth, beginningYear, endMonth, endYear) {
    return getRequest('/api/projects/stats?projectId=' + projectId +
        '&beginningMonth='+ beginningMonth + '&beginningYear='+ beginningYear +
        '&endMonth='+ endMonth + '&endYear='+ endYear);
}

export async function getProjectCollaborators(projectId) {
    return getRequest('/api/projects/collaborators?projectId=' + projectId);
}

export async function createProject(projectName, projectDescription, selectedTags, selectedSkills, action) {
    return postRequest('/api/projects/create?name=' + projectName + '&userId=' + localStorage.getItem('userId'),
        {'projectDescription': projectDescription, 'selectedTags': selectedTags,
        'selectedSkills': selectedSkills}, action);
}

export async function updateProject(projectId, projectName, projectDescription, selectedTags, selectedSkills, action) {
    return postRequest('/api/projects/update?projectId=' + projectId + '&name=' + projectName,
        {'projectDescription': projectDescription, 'selectedTags': selectedTags,
            'selectedSkills': selectedSkills}, action);
}

export async function rateProject(projectId, rating, action) {
    return postRequest('/api/projects/rate?projectId=' + projectId + '&userId=' + localStorage.getItem('userId') +
        '&rating=' + rating,
        {}, action);
}

export async function markAsFinished(projectId, action) {
    return postRequest('/api/projects/finish?projectId=' + projectId,
        {}, action);
}

export async function makeAdmin(collaboratorId, action) {
    return postRequest('/api/projects/make-admin?collaboratorId=' + collaboratorId,
        {}, action);
}

export async function rateCollaborator(collaboratorId, rating, action) {
    return postRequest('/api/projects/rate-colab?collaboratorId=' + collaboratorId + '&rating=' + rating,
        {}, action);
}
