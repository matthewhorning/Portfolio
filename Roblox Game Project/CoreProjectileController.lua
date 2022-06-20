proj_controller = {}
proj_controller.__index = proj_controller

local CoreSystem = game.ReplicatedStorage.CoreSystem;
local Projectiles = CoreSystem.Objects:WaitForChild('GameObjects').Projectiles;
local DebrisService = game:GetService('Debris');
local FractureModule = require(CoreSystem.Modules.ModelControllers.PartFractureModule);
local GameFunctions = require(CoreSystem.Modules.ModelControllers.GameFunctions);

local colors = CoreSystem.ServerData.Colors:GetChildren();
--PRIVATE METHODS


--Initializes a projectile based on the client settings
local function initClient(self, original)
	self.Projectile = original:Clone();
	self.IsLocal = true;
	self.ServerProjectile = original;

	if self.Projectile:FindFirstChild('Visible') then
		local kids = self.Projectile.Visible:GetChildren();
		for i = 1, #kids do
			kids[i].Transparency = 0
		end
	else
		self.Projectile.Transparency = 0;
	end

	--Check for if it's already anchored
	if self.ServerProjectile.Anchored == true then
		self:stop();
	else
		--If it's changed to Anchored, stop the connection
		self.Connection = self.ServerProjectile.Changed:Connect(function()
			if self.ServerProjectile.Anchored then
				self.Connection:Disconnect();
				--Call Stop function
				self:stop();
			end
		end)
	end
end

--Initializes a projectile based on the Server settings
local function initServer(self)
	local findProj = Projectiles:FindFirstChild(tostring(self.ProjectileID));
	if not findProj then
		findProj = Projectiles['-1'];
	end
	self.IsLocal = false;
	self.Projectile = findProj:Clone();

	--Creates a connection for the touched event
	self.Connection = self.Projectile.Touched:Connect(function(hit)
		if self.Projectile and hit and self.CanHit and self:verify(hit) then
			self:hit(hit);
			self.Connection:Disconnect();
		end
	end)
end

--PUBLIC CONSTRUCTOR

--Constructs a Projectile for Player using provided Projectile Number
function proj_controller.new(player, projType, hitP, original)
	local self = {};
	setmetatable(self, proj_controller);

	self.Player = player;
	self.HitAnimEvent = nil;

	--Determine the projectile ID
	self.ProjectileID = self.Player.Character:WaitForChild('Weapon'):WaitForChild('Configuration').WeaponID.Value;

	--Create projectile based on if it's a client or server projectile
	if original then
		initClient(self, original);
	else
		initServer(self);
	end

	--Set Global variables
	self.HitP = hitP;
	self.Handle = self.Player.Character.Weapon.Handle;
	self.Barrel = self.Player.Character.Weapon.Barrel;
	self.Projectile.Name = 'Projectile';

	--Global Booleans
	self.HitDebounce = false;
	self.DamageDebounce = false;
	self.CanHit = false;
	self.Running = false;

	self.Icon = 'rbxassetid://1765935452';
	self.WeaponName = self.Player.Character.Weapon.UI_Info.Info.WeaponName.Value;

	--Set Team and Color Variables
	self.CurrentTeam = self.Player.Character.PlayerConfiguration.CurrentTeam.Value;
	if self.CurrentTeam == 'Red' then
		self.CurrentColor = (CoreSystem.ServerData.GameData.RedColor.Value);
	elseif self.CurrentTeam == 'Blue' then
		self.CurrentColor = (CoreSystem.ServerData.GameData.BlueColor.Value);
	else
		self.CurrentColor = (colors[math.random(1,#colors)].Value)
	end
	self.ColorSequence = ColorSequence.new(self.CurrentColor);

	--Set Dummy variables so nothing returns nil
	--Properties can change within the individual scripts
	self.Damage = 0;
	self.ForceMultiplier = 0;
	self.Hit = nil;
	self.CanTag = false;
	self.TagType = 'nil';
	self.TagTime = 0;
	self.TagAmount = 0;
	
    --Emitter
	self.DecalEvent = CoreSystem:WaitForChild('Remotes'):WaitForChild('Events'):WaitForChild('Server'):WaitForChild('EmitDecal');
	if self.Player.Settings.DecalsEnabled.Value == 1 then
		self.DecalsActive = true;
	else
		self.DecalsActive = false;
	end

	return self
end

--PUBLIC METHODS

--Starts the projectile. Implement within inheriting class.
function proj_controller:start()
	--Class Sub
end

--Stops the projectile. Implement within inheriting class.
function proj_controller:stop()
	--Class Stub
end

--Function for generating FX on the client side. Implement within inheriting class.
function proj_controller:fx()
	--Class Stub
end

--Places the projectile in Workspace and enables hitting
--Can Override
--NOTE: Does not start any Physics or BodyMovers
function proj_controller:spawn()
	--Set to workspace
	self.Projectile.Parent = workspace.Projectiles;
	--Set visible if client, fix Network owner on Server
	if self.IsLocal then
		--Check for whether or not the projectile is already anchored
		if self.Projectile.Anchored == false then
			local effects = self.Projectile.FX:GetChildren();
			if #effects > 0 then
				for i = 1, #effects do
					--TODO: Wrap in pcall, get rid of if
					if effects[i]:IsA('BasePart') then
						effects[i].Effect.Enabled = true
						effects[i].Color = self.CurrentColor;
					else
						effects[i].Enabled = true;
						if not effects[i]:FindFirstChild('DoNotChangeColor') then
							if effects[i]:IsA('ParticleEmitter') then
								effects[i].Color = self.CurrentColor;
							else
								effects[i].Color = self.ColorSequence;
							end
						end
					end
				end
			end
			if self.ServerProjectile.Anchored == true then
				--Call Stop function
				self:stop();
			end
		else
			--It's already hit, so destroy it
			self:fx();
			self:destroy();
		end	
	elseif not self.IsLocal then
		self.Projectile:SetNetworkOwner(nil);
		self.CanHit = true;
		--Play Sounds
		if not self.IsLocal and self.Projectile:WaitForChild('Sounds'):FindFirstChild('InAir') then
			self.Projectile.Sounds.InAir:Play();
		end
	end
	--Add to Debris to ensure deletion
	if self.DespawnTime then
		game:GetService('Debris'):AddItem(self.Projectile, self.DespawnTime);
	else
		game:GetService('Debris'):AddItem(self.Projectile, 12);
	end
end

--Activates the hit function, performing logic for destruction/damage and then deleting metatable.
--Do Not Override
function proj_controller:hit(hit)
	if not self.HitDebounce then
		self.HitDebounce = true;
		--Sound logic
		if self.IsLocal and self.Projectile.Sounds:FindFirstChild('InAir') then
			self.Projectile.Sounds.InAir:Stop();
		end
		self:stop();
		self.Hit = hit;
		--Play the hit sound
		if not self.IsLocal and self.Projectile.Sounds:FindFirstChild('Hit') then
			--Create sound part
			local Emitter = Instance.new('Part');
			Emitter.Name = 'SoundEmitter';
			Emitter.Anchored = true;
			Emitter.CanCollide = false;
			Emitter.Massless = true;
			Emitter.CollisionGroupId = 2;
			Emitter.Position = self.Projectile.Position;
			Emitter.Transparency = 1;
			Emitter.Parent = workspace;
			self.Projectile.Sounds.Hit.Parent = Emitter;
			Emitter.Hit:Play();
			DebrisService:AddItem(Emitter, 1);
		end
		--Global player damage function
		GameFunctions.DamagePlayer(self.Hit, self.Player.Name, self.Projectile, self.Projectile.Configuration.Damage.Value);
		--Break apart glass
		if self.Hit then
			if string.match(self.Hit.Name, 'Glass') then
				local Attachment = Instance.new('Attachment', self.Hit);
				Attachment.Name = 'BreakingPoint';
				Attachment.WorldPosition = self.Projectile.Position;
				FractureModule.FracturePart(self.Hit) -- Shatter the part
			end
		end
		if self.Projectile then
			game:GetService('Debris'):AddItem(self.Projectile, .01);
		end
		if self.Hit:FindFirstChild('Hit') then
			DebrisService:AddItem(self.Hit:FindFirstChild('Hit'), .05);
		end
	end
end

--Verifies whether or not the projectile is allowed to make contact and damage environment
--Checks against collision group and that it is neither the projectile itself nor a player
--Prevents false positives
function proj_controller:verify(hit)
	if hit.CollisionGroupId == 1 then
		return false
	end

	if hit.Name ~= 'Projectile' and hit.Parent ~= self.Player.Character and hit.Name ~= 'Handle' and hit.Name ~= 'SoundEmitter' then
		if hit.Parent.Parent ~= self.Player.Character and hit.Name ~= 'Terrain' and hit.Parent.ClassName ~= 'Accessory' then
			return true
		end
	end
	return false
end

--Name identical to the core roblox function
function proj_controller:destroy()
	--Erases the metatable. No clue if this actually does anything, but I'm not sure how
	--automatic garbage collection in Roblox Lua works with custom objects. In any event, we don't need it anymore
	wait();
	game:GetService('Debris'):AddItem(self.Projectile, .01)
	setmetatable(self, nil);
end

function proj_controller:killCam(config)
	--don't both trying this on the local
	if not self.IsLocal then
		--Fire the event
		CoreSystem.Remotes.Events.Server.CreateKillCam:FireClient(game.Players:GetPlayerFromCharacter(config.Parent), self.Player, self.WeaponName);
		--Teleport the player to a spawn room
		config.Parent.HumanoidRootPart.CFrame = workspace.Spawns.KillCamTeleport.CFrame;
	end
end

--GETTERS

function proj_controller:GetProjectile()
	return self.Projectile;
end

function proj_controller:GetDamage()
	return self.Damage;
end

return proj_controller
