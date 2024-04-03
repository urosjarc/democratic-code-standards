/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PrijavaRes } from '../models/PrijavaRes';
import type { Profil } from '../models/Profil';
import type { UserLoginReq } from '../models/UserLoginReq';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UserService {
    /**
     * @returns Profil OK
     * @throws ApiError
     */
    public static getUser(): CancelablePromise<Profil> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/user',
        });
    }
    /**
     * Getting all user feedbacks.
     * @throws ApiError
     */
    public static getUserFeedback(): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/user/feedback',
        });
    }
    /**
     * Creating new user feedback.
     * @throws ApiError
     */
    public static postUserFeedback(): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/user/feedback',
        });
    }
    /**
     * Access point for user to login.
     * @returns PrijavaRes OK
     * @throws ApiError
     */
    public static postUserLogin({
        requestBody,
    }: {
        requestBody: UserLoginReq,
    }): CancelablePromise<PrijavaRes> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/user/login',
            body: requestBody,
            mediaType: '*/*',
            errors: {
                401: `Unauthorized`,
            },
        });
    }
    /**
     * Action for user logout.
     * @throws ApiError
     */
    public static getUserLogout(): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/user/logout',
        });
    }
}
