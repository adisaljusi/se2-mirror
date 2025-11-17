import axios from "axios";
import { error } from "@sveltejs/kit";
// Load environment variables from .env file for local development
import "dotenv/config";
const API_BASE_URL = process.env.API_BASE_URL; // defined in frontend/.env

export async function load({ url, locals }) {
  const jwtToken = locals.jwt_token;
  const userInfo = locals.user;

  if (!jwtToken) {
    return {
      jobs: [],
      companies: [],
      nrOfPages: 0,
      currentPage: 1,
    };
  }

  try {
    // Get URL parameters for pagination
    const currentPage = parseInt(url.searchParams.get("pageNumber") || "1");
    const pageSize = parseInt(url.searchParams.get("pageSize") || "5");

    // Build query string
    let query = `?pageSize=${pageSize}&pageNumber=${currentPage}`;

    // Could be done in parallel with Promise.all(...)
    const jobsResponse = await axios({
      method: "get",
      url: `${API_BASE_URL}/api/job` + query,
      headers: {
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    let companiesResponse = [];
    if (userInfo && userInfo.user_roles.includes("admin")) {
      companiesResponse = await axios({
        method: "get",
        url: `${API_BASE_URL}/api/company`,
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      });
    }

    return {
      jobs: jobsResponse.data.content,
      companies: companiesResponse.data,
      nrOfPages: jobsResponse.data.totalPages || 0,
      currentPage: currentPage,
    };
  } catch (axiosError) {
    console.log("Error loading companies:", axiosError);
  }
}

export const actions = {
  createJob: async ({ request, locals }) => {
    const jwtToken = locals.jwt_token;

    if (!jwtToken) {
      throw error(401, "Unauthorized");
    }

    const data = await request.formData();
    const job = {
      title: data.get("title"),
      description: data.get("description"),
      earnings: parseFloat(data.get("earnings")),
      jobType: data.get("jobType"),
      companyId: data.get("companyId"),
    };

    try {
      await axios({
        method: "post",
        url: `${API_BASE_URL}/api/job`,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
        data: job,
      });

      return { success: true };
    } catch (error) {
      console.log("Error creating job:", error);
      return { success: false, error: "Could not create job" };
    }
  },

  assignToMe: async ({ request, url, locals }) => {
    const jwtToken = locals.jwt_token;

    if (!jwtToken) {
      throw error(401, "Authentication required");
    }

    const data = await request.formData();
    const jobId = data.get("jobId");

    try {
      await axios({
        method: "put",
        url: `${API_BASE_URL}/api/service/me/assignjob?jobId=${jobId}`,
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
    } catch (error) {
      console.log("Error assigning job:", error);
    }
  },

  completeMyJob: async ({ request, url, locals }) => {
    const jwtToken = locals.jwt_token;

    if (!jwtToken) {
      throw error(401, "Authentication required");
    }

    const data = await request.formData();
    const jobId = data.get("jobId");

    try {
      await axios({
        method: "put",
        url: `${API_BASE_URL}/api/service/me/completejob?jobId=${jobId}`,
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
    } catch (error) {
      console.log("Error completing job:", error);
    }
  },
};
