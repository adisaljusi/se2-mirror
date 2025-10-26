import { error } from "@sveltejs/kit";
import axios from "axios";
import "dotenv/config";
const API_BASE_URL = process.env.API_BASE_URL;

export async function load({ locals }) {
  const jwtToken = locals.jwt_token;

  if (!jwtToken) {
    return {
      companies: [],
    };
  }

  try {
    const companiesResponse = await axios({
      method: "get",
      url: `${API_BASE_URL}/api/company`,
      headers: {
        Authorization: `Bearer ${jwtToken}`,
      },
    });

    return {
      companies: companiesResponse.data,
    };
  } catch (axiosError) {
    console.log("Error loading companies:", axiosError);
  }
}

export const actions = {
  createCompany: async ({ request, locals }) => {
    const jwtToken = locals.jwt_token;

    if (!jwtToken) {
      throw error(401, "Unauthorized");
    }

    const data = await request.formData();
    const company = {
      name: data.get("name"),
      email: data.get("email"),
    };

    try {
      await axios({
        method: "post",
        url: `${API_BASE_URL}/api/company`,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
        data: company,
      });

      return { success: true };
    } catch (error) {
      console.log("Error creating job:", error);
      return { success: false, error: "Could not create job" };
    }
  },
};
