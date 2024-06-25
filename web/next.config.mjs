const BE_API = process.env.BE_API || "http://localhost:8080";

/** @type {import('next').NextConfig} */
const nextConfig = {
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: `${BE_API}/api/:path*`,
            }
        ];
    },
    reactStrictMode: false
}

export default nextConfig;
