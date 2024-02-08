<img src="logo_GeoAid.png" width="100" height="100" alt="GeoAid">
<h1>GeoAid</h1>

![repo status](https://img.shields.io/badge/repo%20status-active-green?labelColor=gray&style=flat)
![build](https://img.shields.io/badge/build-passing-green?labelColor=gray&style=flat)
![pipeline](https://img.shields.io/badge/pipeline-passed-green?labelColor=gray&style=flat)


<h2>Table of contents</h2>

<ul>
    <li>What is GeoAid</li>
    <li>Features</li>
    <li>Business case & Business site</li>
    <li>Schematics</li>
    <li>Wireframes</li>
    <li>Self study topics</li>
    <li>Client</li>
    <li>Server</li>
    <li>Install</li>
    <li>Live Demo</li>
    <li>Support</li>
</ul>

---

<h2>What is GeoAid?</h2>


<p>GeoAid is an application that enables people to look at maps of the area in domes and to see which areas have been claimed by who and what sector. It also shows other data in a heatmap such as crime rates, commerce and other interesting data.
GeoAid also consults new companies and families to find a new location for a home or business. GeoAid gathers all kinds of data and puts this in graphs, the end user is able to filter this data easily.<p>

<img src="screenshot.png" alt="screenshot of the crime heatmap">

<h2>Features<h2>

---

<ul>
    <li>Heatmaps</li>
    <li>Filters on the map</li>
    <li>Graphs and pies</li>
    <li>Contact to our localisation office</li>
    <li>Make appointments</li>
    <li>Premium</li>
    <ul>
        <li>Standard</li>
        <li>Company</li>
    </ul>
    <li>Warning based on location and crime heatmaps</li>
</ul>

---

<h2>Business case & Business site</h2>

<p>A business case is a document that outlines the justification for a proposed project or venture. It presents a clear and concise analysis of the potential costs, benefits, and risks associated with the project and is used to persuade decision makers to invest resources in it. The business case typically includes a description of the problem or opportunity being addressed, an analysis of potential solutions, a financial analysis, and a recommendation for a course of action. The goal of a business case is to provide a logical and well-reasoned argument for why a particular project or venture should be undertaken, and to help decision makers understand the potential impact of the project on the organization.</p>


Links:
<ul>
<li>

[Business case](https://docs.google.com/document/d/1aOWMkqqqwTaVaFg0CQ6nP291ewx3OkM0_9wAl1rj1aM/edit?usp=sharing)

</li>

<li>

[Calculations](https://docs.google.com/spreadsheets/d/1j7P3sXgjzEFpmTfvqTn2EO1vrBAVQWqA2PC9lGo-A-U/edit?usp=sharing)

</li>

<li>

[Business site](https://lucasguillemyn.wixsite.com/geoaid)

</li>
</ul>

---

<h2>Schematics</h2>

**Flow charts**

[Link](https://lucid.app/lucidchart/c82fa5da-9519-4f2e-943c-ed9d218fdf70/edit?viewport_loc=-399%2C-44%2C3076%2C1477%2CVXK72F4TZo00&invitationId=inv_6ece07b8-a0af-4316-aade-feb5a503b515)

**C4**

[Link](https://lucid.app/lucidchart/467d4cdf-46e5-47da-8a14-4f9fc0e924be/edit?viewport_loc=-1346%2C-1752%2C6307%2C3028%2C0_0&invitationId=inv_44efe0f6-c8f0-4607-87d9-9a1f37673203)

**User stories**

[Link](https://docs.google.com/document/d/1p_8MVqby21ReAk4D_uJA2KwqwmFOI79x-1J_GY9bBFo/edit?usp=sharing)

**UCD**

[Link](https://lucid.app/lucidchart/f533c7be-52b8-4f86-9a94-10919a58e891/edit?viewport_loc=155%2C-1200%2C2342%2C1125%2C-lL7_BCARhL~&invitationId=inv_7682bf47-8142-4742-98c0-2c25250d6e37)

**ERD**

[Link](https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-18/documentation/-/raw/main/Schematics/ERD/ERD%20GeoAid.png)


**OpenAPI-spec**

This is the [Link](https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-18/documentation/-/blob/main/api-spec/openapi-mars.yaml) to our OpenAPI-spec.

There is a total of 12 endpoints:

<ul>
<li>/domes Get all the domes</li>
<li>/users Get all users</li>
<li>/companies Get all companies</li>
<li>/company/{userId} Get a company by userId</li>
<li>/oxygenleaks Get all oxygenLeaks</li>
<li>/population Get population</li>
<li>/appointments Get appointments</li>
<li>/appointment Make an appointment</li>
<li>/appointment/{appointmentId} Delete appointment</li>
<li>/medicalDispatches Get all medicalDispatches</li>
<li>/meteorShowers Get all meteor showers</li>
<li>/dustStorms Get all dust storms</li>
</ul>

---

<h2>Wireframes</h2>

This is the [Link](https://www.figma.com/file/v5J4uN1c8u7kgVgVku6ips/GeoAid?node-id=0%3A1&t=Hn7WOlpjgEcNHkfR-1) to our Wireframes.

Here are the user tests: [User tests](https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-18/documentation/-/raw/main/Usability%20Test/Moderated_usability_tests_1.docx)

Find the [Recording](https://www.youtube.com/watch?v=AIKJZnzfyks) here.

---

<h2>Self study topics</h2>

<ul>
<li>Fullscreen API: You are able to see the graphs in fullscreen.</li>
<li>Css Animations: On every page there are some animations.</li>
<li>Graphs & Maps: The first screen on the application is a map, the graphs are all the stats.</li>
<li>SASS: We made this project with SASS.</li>
</ul>


---

<h2>Client</h2>

[Link](README_client.md) to client read me.

---

<h2>Server</h2>

[Link](README_server.md) to server read me.

---

<h2>Install</h2>

Get the frontend code:

```
git clone https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-18/client.git
```

Get the backend code:

```
git clone https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-18/server.git
```

Change the settings in the config.json file to run locally.

Dir: **client/src/config.json**

Change the host with the running backend host.

You will also need to change the config.json file in the backend code.

Dir: **server/src/conf/config.json**

Here you can change the API url.
The local url is this: https://git.ti.howest.be/TI/2022-2023/s3/analysis-and-development-project/projects/group-18/documentation/-/raw/main/api-spec/openapi-mars.yaml


Make sure these 2 directories are in the same root folder.

---

<h2>Live demo</h2>

https://project-ii.ti.howest.be/mars-18/

This is the [Recording](https://www.youtube.com/watch?v=XKD-m16nCY4) of the live demo.

---

<h2>Support</h2>
image.pngimage.pngimage.pngimage.png
<p>To get basic support you can contact use at GeoAid@gmail.com</p>


