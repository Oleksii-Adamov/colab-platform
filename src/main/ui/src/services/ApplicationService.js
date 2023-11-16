import getRequest from "@/services/getRequest";
import postRequest from "@/services/postRequest";

export async function getProjectPendingApplications(projectId) {
    return getRequest('/api/applications/project-pending-applications?projectId='+projectId);
}

export async function createApplication(projectId, action) {
    return postRequest('/api/applications/create?userId=' + localStorage.getItem('userId')
        + '&projectId=' + projectId, {}, action);
}

export async function approveApplication(applicationId, applicantUserId, projectId, action) {
    return postRequest('/api/applications/approve?applicationId=' + applicationId +
        '&userId=' + applicantUserId
        + '&projectId=' + projectId, {}, action);
}

export async function rejectApplication(applicationId, action) {
    return postRequest('/api/applications/reject?applicationId=' + applicationId, {}, action);
}