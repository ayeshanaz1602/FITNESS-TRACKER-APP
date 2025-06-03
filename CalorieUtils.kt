fun distanceToCalories(distanceMeters: Float): Float {
    return (distanceMeters / 1000) * 60f // average burn: 60 kcal/km
}
