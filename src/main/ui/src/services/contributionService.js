import postRequest from "@/services/postRequest";
import getRequest from "@/services/getRequest";

export async function contribute(contributionDescription, contributionValue, action) {
    let projectId = localStorage.getItem('projectId')
    console.log(projectId)
    console.log(typeof(projectId))
    if (projectId !== undefined && projectId !== null && projectId !== "") {
        return postRequest('/api/contributions/create?' + 'userId=' + localStorage.getItem('userId') +
            '&projectId=' + projectId,
            {'contributionDescription': contributionDescription, 'contributionValue': contributionValue},
                action);
    }
    else {
        alert('Please go to project page first')
    }
}

export async function getProjectPendingContributions(projectId) {
    return getRequest('/api/contributions/project-pending-contributions?projectId='+projectId);
}

export async function getProjectApprovedContributions(projectId) {
    return getRequest('/api/contributions/project-approved-contributions?projectId='+projectId);
}

export async function approveContribution(contributionId, action) {
    return postRequest('/api/contributions/approve?contributionId=' + contributionId , {}, action);
}

export async function rejectContribution(contributionId, action) {
    return postRequest('/api/contributions/reject?contributionId=' + contributionId, {}, action);
}