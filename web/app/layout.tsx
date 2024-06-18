import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import React from "react";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "pi-monitor",
  description: "An monitoring tool for your host and services.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`w-screen h-screen overflow-y-auto overflow-x-hidden bg-white ${inter.className}`}>
      {children}
      </body>
    </html>
  );
}
