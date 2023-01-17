package org.basmat.cell.data;

public enum ECellType {
	/**
	 * This Enum class contains the reference URI for a texture, as well as any associated descriptions and names.
	 */
	
	BASE_PATH {
		@Override
		public String getPath() {
			return "./assets/";
		}
		@Override
		public
		String getCellDescription() {
			return null;
		}

		@Override
		public String getCellName() {
			return null;
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	DEEP_WATER {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "deep_water.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Deep water, not crossable";
		}

		@Override
		public String getCellName() {
			return "Deep water";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	WATER {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "water.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Medium-depth water, not crossable";
		}

		@Override
		public String getCellName() {
			return "Water";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	LIGHT_WATER {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "light_water.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Shallow water, crossable for most";
		}

		@Override
		public String getCellName() {
			return "Shallow Water";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	MOUNTAIN_BASE {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "mountain_base.png";
		}
		@Override
		public
		String getCellDescription() {
			return "A base of a mountain, gravelly";
		}

		@Override
		public String getCellName() {
			return "Mountain Base";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	MOUNTAIN_BODY {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "mountain_body.png";
		}
		@Override
		public
		String getCellDescription() {
			return "The body of a mountain, very rocky";
		}

		@Override
		public String getCellName() {
			return "Mountain body";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	MOUNTAIN_PEAK {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "mountain_peak.png";
		}
		@Override
		public
		String getCellDescription() {
			return "A peak of a mountain";
		}

		@Override
		public String getCellName() {
			return "Mountain peak";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	GRASS {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "grass.png";
		}
		@Override
		public
		String getCellDescription() {
			return "Grassy fields, easily traversable";
		}

		@Override
		public String getCellName() {
			return "Grass";
		}

		@Override
		public
		boolean isHabitable() {
			return true;
		}
	},
	SAND {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "sand.png";
		}
		@Override
		public
		String getCellDescription() {
			return "The shore of a sea or river";
		}
		@Override
		public String getCellName() {
			return "Sand";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	SOCIETYBLOCK {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "society.png";
		}
		@Override
		public
		String getCellDescription() {
			return "A town hall";
		}

		@Override
		public String getCellName() {
			return "Society Block";
		}

		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	NUTRIENTS{
		@Override
		public String getPath() {
			return BASE_PATH.getPath() + "nutrient.png";
		}

		@Override
		public String getCellDescription() {
			return "A bush of berries";
		}

		@Override
		public String getCellName() {
			return "Food Source";
		}

		@Override
		public boolean isHabitable() {
			return true;
		}
	},
	MISSING_TEXTURE{
		@Override
		public String getPath() {
			return BASE_PATH.getPath() + "missing_texture.png";
		}

		@Override
		public String getCellDescription() {
			return "Missing texture";
		}

		@Override
		public String getCellName() {
			return "Missing texture";
		}

		@Override
		public boolean isHabitable() {
			return false;
		}
	};


	
	abstract public String getPath();
	abstract public  String getCellDescription();
	abstract public String getCellName();
	abstract public boolean isHabitable();
}
