package com.example.ccred_3.data

data class CarbonSavingsData(
    val ministry: String,
    val totalCarbonSaved: Double, // in tons
    val yearlyData: List<YearlyCarbonData>,
    val initiatives: List<String>,
    val description: String
)

data class YearlyCarbonData(
    val year: Int,
    val carbonSaved: Double, // in tons
    val initiatives: List<String>
)

// Dummy data for testing
object DummyCarbonData {
    val ministriesData = listOf(
        CarbonSavingsData(
            ministry = "Ministry of Environment",
            totalCarbonSaved = 1250.5,
            yearlyData = listOf(
                YearlyCarbonData(2020, 200.0, listOf("Green Energy Initiative", "Waste Reduction Program")),
                YearlyCarbonData(2021, 350.0, listOf("Solar Panel Installation", "Electric Vehicle Fleet")),
                YearlyCarbonData(2022, 450.0, listOf("Carbon Capture Technology", "Forest Conservation")),
                YearlyCarbonData(2023, 250.5, listOf("Renewable Energy Projects", "Sustainable Agriculture"))
            ),
            initiatives = listOf(
                "Green Energy Initiative",
                "Waste Reduction Program", 
                "Solar Panel Installation",
                "Electric Vehicle Fleet",
                "Carbon Capture Technology",
                "Forest Conservation",
                "Renewable Energy Projects",
                "Sustainable Agriculture"
            ),
            description = "Leading environmental conservation efforts through innovative green technologies and sustainable practices."
        ),
        CarbonSavingsData(
            ministry = "Ministry of Energy",
            totalCarbonSaved = 2100.3,
            yearlyData = listOf(
                YearlyCarbonData(2020, 300.0, listOf("Wind Farm Development", "Energy Efficiency Programs")),
                YearlyCarbonData(2021, 500.0, listOf("Hydroelectric Projects", "Smart Grid Implementation")),
                YearlyCarbonData(2022, 700.0, listOf("Nuclear Energy Expansion", "Battery Storage Systems")),
                YearlyCarbonData(2023, 600.3, listOf("Geothermal Energy", "Energy Storage Solutions"))
            ),
            initiatives = listOf(
                "Wind Farm Development",
                "Energy Efficiency Programs",
                "Hydroelectric Projects", 
                "Smart Grid Implementation",
                "Nuclear Energy Expansion",
                "Battery Storage Systems",
                "Geothermal Energy",
                "Energy Storage Solutions"
            ),
            description = "Transforming the energy sector with clean and renewable energy sources to reduce carbon footprint."
        ),
        CarbonSavingsData(
            ministry = "Ministry of Transport",
            totalCarbonSaved = 1800.7,
            yearlyData = listOf(
                YearlyCarbonData(2020, 250.0, listOf("Electric Bus Fleet", "Bike Lane Infrastructure")),
                YearlyCarbonData(2021, 400.0, listOf("High-Speed Rail", "Electric Vehicle Charging Stations")),
                YearlyCarbonData(2022, 600.0, listOf("Public Transport Modernization", "Green Logistics")),
                YearlyCarbonData(2023, 550.7, listOf("Autonomous Electric Vehicles", "Smart Traffic Management"))
            ),
            initiatives = listOf(
                "Electric Bus Fleet",
                "Bike Lane Infrastructure",
                "High-Speed Rail",
                "Electric Vehicle Charging Stations",
                "Public Transport Modernization",
                "Green Logistics",
                "Autonomous Electric Vehicles",
                "Smart Traffic Management"
            ),
            description = "Revolutionizing transportation with sustainable mobility solutions and green infrastructure."
        ),
        CarbonSavingsData(
            ministry = "Ministry of Agriculture",
            totalCarbonSaved = 950.2,
            yearlyData = listOf(
                YearlyCarbonData(2020, 150.0, listOf("Organic Farming Promotion", "Soil Carbon Sequestration")),
                YearlyCarbonData(2021, 250.0, listOf("Precision Agriculture", "Agroforestry Programs")),
                YearlyCarbonData(2022, 300.0, listOf("Sustainable Irrigation", "Crop Rotation Systems")),
                YearlyCarbonData(2023, 250.2, listOf("Vertical Farming", "Carbon-Neutral Livestock"))
            ),
            initiatives = listOf(
                "Organic Farming Promotion",
                "Soil Carbon Sequestration",
                "Precision Agriculture",
                "Agroforestry Programs",
                "Sustainable Irrigation",
                "Crop Rotation Systems",
                "Vertical Farming",
                "Carbon-Neutral Livestock"
            ),
            description = "Promoting sustainable agricultural practices that enhance carbon sequestration and reduce emissions."
        ),
        CarbonSavingsData(
            ministry = "Ministry of Industry",
            totalCarbonSaved = 1650.8,
            yearlyData = listOf(
                YearlyCarbonData(2020, 200.0, listOf("Industrial Energy Efficiency", "Circular Economy Initiatives")),
                YearlyCarbonData(2021, 400.0, listOf("Green Manufacturing", "Waste-to-Energy Plants")),
                YearlyCarbonData(2022, 550.0, listOf("Carbon Capture in Industry", "Sustainable Materials")),
                YearlyCarbonData(2023, 500.8, listOf("Smart Manufacturing", "Zero-Waste Production"))
            ),
            initiatives = listOf(
                "Industrial Energy Efficiency",
                "Circular Economy Initiatives",
                "Green Manufacturing",
                "Waste-to-Energy Plants",
                "Carbon Capture in Industry",
                "Sustainable Materials",
                "Smart Manufacturing",
                "Zero-Waste Production"
            ),
            description = "Transforming industrial processes to be more sustainable and carbon-efficient through innovation."
        )
    )
}
