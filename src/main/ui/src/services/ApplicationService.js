import getRequest from "@/services/getRequest";
import postRequest from "@/services/postRequest";

export async function getProjectApplications(projectId) {
    return getRequest('/api/applications/project-applications?projectId='+projectId);
}

export async function createApplication(projectId) {
    return postRequest('/api/applications/create?userId=' + localStorage.getItem('userId')
        + '&projectId=' + projectId);
}

export async function approveApplication(applicationId, applicantUserId, projectId) {
    return postRequest('/api/applications/approve?applicationId=' + applicationId +
        '&userId=' + applicantUserId
        + '&projectId=' + projectId);
}

export async function rejectApplication(applicationId) {
    return postRequest('/api/applications/reject?applicationId=' + applicationId);
}