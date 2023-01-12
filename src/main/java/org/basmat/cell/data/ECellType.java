package org.basmat.cell.data;

public enum ECellType {
	/**
	 * This Enum class contains the reference URI for a texture and provides a method to return it
	 */
	
	BASE_PATH {
		@Override
		public String getPath() {
			return "./assets/";
		}
		@Override
		public
		String getLocalizedName() {
			return null;
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	SEA {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "sea.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "River";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
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
		String getLocalizedName() {
			return "Sand";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	UNINHABITABLE {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "uninhabitable.png";
		}
		@Override
		public
		String getLocalizedName() {
			return "Untraversable terrain";
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
		String getLocalizedName() {
			return "Grass";
		}
		@Override
		public
		boolean isHabitable() {
			return true;
		}
	},
	GRADIENT {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "gradient.png";
		}		@Override
		public
		String getLocalizedName() {
			return "Patchy grass";
		}
		@Override
		public
		boolean isHabitable() {
			return true;
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
		String getLocalizedName() {
			return "Society Block";
		}
		@Override
		public
		boolean isHabitable() {
			return false;
		}
	},
	SOCIETYRADIUS {
		@Override
		public
		String getPath() {
			return BASE_PATH.getPath() + "society_radius.png";
		}		
		@Override
		public
		String getLocalizedName() {
			return "Society Radius";
		}
		@Override
		public
		boolean isHabitable() {
			return true;
		}
	},
	NUTRIENTS{
		@Override
		public String getPath() {
			return BASE_PATH.getPath() + "nutrient.png";
		}

		@Override
		public String getLocalizedName() {
			return "A bush of berries";
		}

		@Override
		public boolean isHabitable() {
			return true;
		}
	};


	
	abstract public String getPath();
	abstract public  String getLocalizedName();
	abstract public boolean isHabitable();
}
