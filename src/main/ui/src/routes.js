import LandingPage from "@/pages/LandingPage";
import ProjectPage from "@/pages/ProjectPage";
import MyProjectsPage from "@/pages/MyProjectsPage";
import CreateProjectPage from "@/pages/CreateProjectPage";
import ProjectSearchPage from "@/pages/ProjectSearchPage";
import ContributePage from "@/pages/ContributePage";
import ApplicationsPage from "@/pages/ApplicationsPage";
import PendingContributionsPage from "@/pages/PendingContributionsPage";


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
        path: '/',
        component: LandingPage
    },
    {
        path: '/:unused*',
        redirect: '/',
    }
]