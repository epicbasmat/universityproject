package org.basmat.map.util;

import java.io.Serializable;

/**
 * Contains the final properties for the simulation.
 * @param societyCount The amount of societies to set in the simulation
 * @param nutrientCount The amount of nutrient cells to apply to the simulation
 * @param initialNutrientCount The initial amount of nutrient cells to apply to every society cell
 * @param attritionThreshold The threshold that life cells have before they die due to attrition
 * @param overcrowdThreshold The threshold that life cells have before they die of overcrowding
 * @param landRatio The land to life cell ratio that all societies have before they collapse
 * @param foodThreshold The food to life cell ratio that all societies have before they kill a life cell.
 */
public record SimulationProperties(int societyCount, int nutrientCount, int initialNutrientCount, int attritionThreshold, int overcrowdThreshold, int landRatio, Double foodThreshold) implements Serializable { }
