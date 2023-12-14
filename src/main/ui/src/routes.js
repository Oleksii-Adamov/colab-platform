import LandingPage from "@/pages/LandingPage";
import ProjectPage from "@/pages/ProjectPage";
import MyProjectsPage from "@/pages/MyProjectsPage";
import CreateProjectPage from "@/pages/CreateProjectPage";
import ProjectSearchPage from "@/pages/ProjectSearchPage";
import ContributePage from "@/pages/ContributePage";
import ApplicationsPage from "@/pages/ApplicationsPage";
import PendingContributionsPage from "@/pages/PendingContributionsPage";
import UpdateProjectPage from "@/pages/UpdateProjectPage";
import ProfilePage from "@/pages/ProfilePage";
import UpdateProfilePage from "@/pages/UpdateProfilePage";
import ProjectStatsPage from "@/pages/ProjectStatsPage";
import AdminPage from "@/pages/AdminPage";
import CollaboratorsPage from "@/pages/CollaboratorsPage";


export default [
    {
        path: '/create-project',
        component: CreateProjectPage,
        name: 'create-project-page'
    },
    {
        path: '/my-projects',
        component: MyProjectsPage,
        name: 'my-projects-page'
    },
    {
        path: '/project-search',
        component: ProjectSearchPage,
        name: 'project-search-page'
    },
    {
        path: '/projects/:id',
        name: 'project-page',
        component: ProjectPage,
        props: true,
    },
    {
        path: '/contribute',
        name: 'contribute-page',
        component: ContributePage,
    },
    {
        path: '/projects/applications',
        name: 'applications-page',
        component: ApplicationsPage,
    },
    {
        path: '/projects/pending-contributions',
        name: 'pending-contributions-page',
        component: PendingContributionsPage,
    },
    {
        path: '/projects/collaborators',
        name: 'collaborators-page',
        component: CollaboratorsPage,
    },
    {
        path: '/projects/edit',
        name: 'edit-project-page',
        component: UpdateProjectPage,
    },
    {
        path: '/users/:id',
        name: 'profile-page',
        component: ProfilePage,
        props: true,
    },
    {
        path: '/edit-profile',
        name: 'edit-profile-page',
        component: UpdateProfilePage
    },
    {
        path: '/projects/stats',
        name: 'project-stats-page',
        component: ProjectStatsPage
    },
    {
        path: '/admin-page',
        name: 'admin-page',
        component: AdminPage
    },
    {
        path: '/',
        component: LandingPage
    },
    {
        path: '/:unused*',
        redirect: '/',
    }
]